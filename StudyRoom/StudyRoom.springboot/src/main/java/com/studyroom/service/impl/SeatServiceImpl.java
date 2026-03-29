package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyroom.dto.SeatArrange;
import com.studyroom.dto.SeatDto;
import com.studyroom.dto.query.SeatPagedInput;
import com.studyroom.entity.AppUser;
import com.studyroom.entity.AppointRecord;
import com.studyroom.entity.Room;
import com.studyroom.entity.Seat;
import com.studyroom.enums.AppointDateTypeEnum;
import com.studyroom.enums.AppointStatusEnum;
import com.studyroom.mapper.AppUserMapper;
import com.studyroom.mapper.AppointRecordMapper;
import com.studyroom.mapper.RoomMapper;
import com.studyroom.mapper.SeatMapper;
import com.studyroom.service.SeatService;
import com.studyroom.tools.Extension;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import com.studyroom.tools.exception.CustomException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 座位功能实现类
 */
@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper _AppUserMapper;
    /**
     * 操作数据库的Seat表mapper对象
     */
    @Autowired
    private SeatMapper _SeatMpper;
    @Autowired
    private RoomMapper _RoomMapper;


    @Autowired
    private AppointRecordMapper _AppointRecordMapper;


    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<Seat> BuilderQuery(SeatPagedInput input) {
        //声明一个支持座位查询的(拉姆达)表达式
        LambdaQueryWrapper<Seat> queryWrapper = Wrappers.<Seat>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, Seat::getId, input.getId())
                .eq(input.getCreatorId() != null, Seat::getCreatorId, input.getCreatorId());
        //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getNoLike())) {
            queryWrapper = queryWrapper.like(Seat::getNo, input.getNoLike());
        }

        if (input.getRoomId() != null) {
            queryWrapper = queryWrapper.eq(Seat::getRoomId, input.getRoomId());
        }
        if (input.getIsMaintain() != null) {
            queryWrapper = queryWrapper.eq(Seat::getIsMaintain, input.getIsMaintain());
        }
        return queryWrapper;
    }

    /**
     * 处理座位对于的外键数据
     */
    private List<SeatDto> DispatchItem(List<SeatDto> items) throws InvocationTargetException, IllegalAccessException {

        for (SeatDto item : items) {
            //查询出关联的创建用户信息

            AppUser CreatorAppUserEntity = _AppUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getId, item.getCreatorId())).stream().findFirst().orElse(new AppUser());
            item.setCreatorAppUserDto(CreatorAppUserEntity.MapToDto());

            //查询出关联的Room表信息
            Room RoomEntity = _RoomMapper.selectList(Wrappers.<Room>lambdaQuery().eq(Room::getId, item.getRoomId())).stream().findFirst().orElse(new Room());
            item.setRoomDto(RoomEntity.MapToDto());


            List<AppointRecord> appointRecords = _AppointRecordMapper.selectList(Wrappers.<AppointRecord>lambdaQuery().eq(AppointRecord::getSeatId, item.getId()));

            //总评论个数
            long commentCount = appointRecords.stream().filter(x -> x.getAppointStatus() == AppointStatusEnum.预约完成.index()).count();
            item.setTotalCommentCount((int) commentCount);

            //总使用次数
            long useCount = appointRecords.stream().filter(x -> x.getAppointStatus() == AppointStatusEnum.预约完成.index() || x.getAppointStatus() == AppointStatusEnum.待评论.index()).count();
            item.setTotalUseCount((int) useCount);

            //求评分平均值
            double agvCommentScore = appointRecords.stream().filter(x -> x.getAppointStatus() == AppointStatusEnum.预约完成.index()).mapToDouble(x -> x.getCommentScore()).average().orElse(0);
            //保留2未小数
            DecimalFormat df = new DecimalFormat("#.00");
            agvCommentScore = Double.parseDouble(df.format(agvCommentScore));
            item.setAgvCommentScore(agvCommentScore);


        }
        return items;
    }

    /**
     * 座位分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<SeatDto> List(SeatPagedInput input) {
        //构建where条件+排序
        LambdaQueryWrapper<Seat> queryWrapper = BuilderQuery(input);

        //按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(Seat::getCreationTime);
        //构建一个分页查询的model
        Page<Seat> page = new Page<>(input.getPage(), input.getLimit());
        //从数据库进行分页查询获取座位数据
        IPage<Seat> pageRecords = _SeatMpper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount = _SeatMpper.selectCount(queryWrapper);
        //把Seat实体转换成Seat传输模型
        List<SeatDto> items = Extension.copyBeanList(pageRecords.getRecords(), SeatDto.class);
        //计算表的数据
        items = DispatchItem(items);


        //返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个座位查询
     */
    @SneakyThrows
    @Override
    public SeatDto Get(SeatPagedInput input) {
        if (input.getId() == null) {
            return new SeatDto();
        }
        PagedResult<SeatDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new SeatDto());
    }


    /**
     * 检测座位是否相同
     *
     * @param input
     */
    private void CheckSeatIsSame(SeatDto input) {
        Seat seat = _SeatMpper.selectList(Wrappers.<Seat>lambdaQuery()
                .ne(input.getId() != null, Seat::getId, input.getId())
                .eq(Seat::getSRow, input.getSRow())
                .eq(Seat::getSCol, input.getSCol())
                .eq(Seat::getRoomId, input.getRoomId())).stream().findFirst().orElse(null);
        if (seat != null) {
            throw new CustomException("该自习室下已存在相同的座位号:" + seat.getNo());
        }
    }

    /**
     * 座位创建或者修改
     */
    @SneakyThrows
    @Override
    public SeatDto CreateOrEdit(SeatDto input) {

        this.CheckSeatIsSame(input);
        input.setNo("S" + "-" + input.getSRow() + "-" + input.getSCol());
        //声明一个座位实体
        Seat Seat = input.MapToEntity();
        //调用数据库的增加或者修改方法
        saveOrUpdate(Seat);
        //把传输模型返回给前端
        return Seat.MapToDto();
    }


    /**
     * 批量创建座位
     */
    @SneakyThrows
    @Override
    public void BatchCreate(List<SeatDto> inputs) {

        //因为我们没有事务的概念 先进行判断 如果集合的数据都满足条件 则不报错
        for (SeatDto input : inputs) {
            CheckSeatIsSame(input);
        }

        for (SeatDto input : inputs) {
            input.setNo("S" + "-" + input.getSRow() + "-" + input.getSCol());
        }
        List<Seat> seats = Extension.copyBeanList(inputs, Seat.class);

        //一次性插入
        saveBatch(seats);

    }


    /**
     * 座位删除
     */
    @Override
    public void Delete(IdInput input) {
        Seat entity = _SeatMpper.selectById(input.getId());
        _SeatMpper.deleteById(entity);
    }

    /**
     * 座位批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }

    /**
     * 得到今天到未来30天的日期
     */
    @Override
    public List<LocalDate> GetSevenDays() {

        //得到今天到未来7天的日期集合
        List<LocalDate> result = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            result.add(LocalDate.now().plusDays(i));
        }
        return result;
    }

    /**
     * 得到选中日期的座位信息
     */
    @Override
    public SeatArrange GetArrange(SeatPagedInput input) throws InvocationTargetException, IllegalAccessException {


        //得到自习室下面所有的座位信息
        List<Seat> seats = _SeatMpper.selectList(Wrappers.<Seat>lambdaQuery().eq(Seat::getRoomId, input.getRoomId()));


        //查询选择日期的预约情况
        List<AppointRecord> appointRecords = _AppointRecordMapper.selectList(
                Wrappers.<AppointRecord>lambdaQuery().eq(AppointRecord::getRoomId, input.getRoomId())
                        .ge(AppointRecord::getAppointDate, input.getSelectDate())
                        .lt(AppointRecord::getAppointDate, input.getSelectDate().plusDays(1)));

        List<List<SeatDto>> amSeatDtoList = new ArrayList<>();
        List<List<SeatDto>> pmSeatDtoList = new ArrayList<>();
        List<List<SeatDto>> nmSeatDtoList = new ArrayList<>();

        //得到seat中最大的行和列
        int maxRow = seats.stream().mapToInt(Seat::getSRow).max().orElse(0);
        int maxCol = seats.stream().mapToInt(Seat::getSCol).max().orElse(0);
        for (int i = 1; i <= maxRow; i++) {
            int row = i;
            List<SeatDto> amSeatRowDtoList = new ArrayList<>();
            List<SeatDto> pmSeatRowDtoList = new ArrayList<>();
            List<SeatDto> nmSeatRowDtoList = new ArrayList<>();
            //6点到12是中午  14点到18点是下午    19点到23点是夜晚
            for (int j = 1; j <= maxCol; j++) {
                int col = j;
                var seat = seats.stream().filter(x -> x.getSRow() == row && x.getSCol() == col).findFirst().orElse(new Seat());

                //处理上午
                SeatDto amSeatDto = seat.MapToDto();
                //默认就是未占用
                amSeatDto.setIsOccupy(false);
                if (Extension.isNullOrZero(amSeatDto.getId()) == false) {
                    long count = appointRecords.stream()
                            .filter(x -> x.getSeatId().equals(amSeatDto.getId()) && x.getAppointDateType() == AppointDateTypeEnum.上午.index())
                            .filter(x -> (x.getAppointStatus() == AppointStatusEnum.使用中.index()
                                    || x.getAppointStatus() == AppointStatusEnum.待使用.index()
                                   ))
                            .count();
                    amSeatDto.setIsOccupy(count > 0);
                }
                amSeatRowDtoList.add(amSeatDto);

                //处理下午
                SeatDto pmSeatDto = seat.MapToDto();
                //默认就是未占用
                pmSeatDto.setIsOccupy(false);
                if (Extension.isNullOrZero(pmSeatDto.getId()) == false) {
                    long count = appointRecords.stream()
                            .filter(x -> x.getSeatId().equals(amSeatDto.getId()) && x.getAppointDateType() == AppointDateTypeEnum.下午.index())
                            .filter(x -> (x.getAppointStatus() == AppointStatusEnum.使用中.index()
                                    || x.getAppointStatus() == AppointStatusEnum.待使用.index()
                                   ))
                            .count();
                    pmSeatDto.setIsOccupy(count > 0);
                }
                pmSeatRowDtoList.add(pmSeatDto);


                //处理夜晚
                SeatDto nmSeatDto = seat.MapToDto();
                //默认就是未占用
                nmSeatDto.setIsOccupy(false);
                if (Extension.isNullOrZero(nmSeatDto.getId()) == false) {
                    long count = appointRecords.stream()
                            .filter(x -> x.getSeatId().equals(amSeatDto.getId()) && x.getAppointDateType() == AppointDateTypeEnum.夜晚.index())
                            .filter(x -> (x.getAppointStatus() == AppointStatusEnum.使用中.index()
                                    || x.getAppointStatus() == AppointStatusEnum.待使用.index()
                                 ))
                            .count();
                    nmSeatDto.setIsOccupy(count > 0);
                }
                nmSeatRowDtoList.add(nmSeatDto);
            }
            amSeatDtoList.add(amSeatRowDtoList);
            pmSeatDtoList.add(pmSeatRowDtoList);
            nmSeatDtoList.add(nmSeatRowDtoList);
        }

        SeatArrange seatArrange = new SeatArrange();
        seatArrange.setAmSeatDtoList(amSeatDtoList);
        seatArrange.setPmSeatDtoList(pmSeatDtoList);
        seatArrange.setNmSeatDtoList(nmSeatDtoList);
        return seatArrange;

    }


}
