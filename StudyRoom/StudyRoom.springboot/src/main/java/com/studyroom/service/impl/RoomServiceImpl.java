package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyroom.dto.RoomDto;
import com.studyroom.dto.query.RoomPagedInput;
import com.studyroom.entity.AppUser;
import com.studyroom.entity.AppointRecord;
import com.studyroom.entity.Room;
import com.studyroom.entity.Seat;
import com.studyroom.enums.AppointStatusEnum;
import com.studyroom.mapper.AppUserMapper;
import com.studyroom.mapper.AppointRecordMapper;
import com.studyroom.mapper.RoomMapper;
import com.studyroom.mapper.SeatMapper;
import com.studyroom.service.RoomService;
import com.studyroom.tools.Extension;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 自习室功能实现类
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper _AppUserMapper;
    /**
     * 操作数据库的Room表mapper对象
     */
    @Autowired
    private RoomMapper _RoomMpper;


    @Autowired
    private SeatMapper _SeatMapper;


    @Autowired
    private AppointRecordMapper _AppointRecordMapper;


    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<Room> BuilderQuery(RoomPagedInput input) {
        //声明一个支持自习室查询的(拉姆达)表达式
        LambdaQueryWrapper<Room> queryWrapper = Wrappers.<Room>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, Room::getId, input.getId())
                .eq(input.getCreatorId() != null, Room::getCreatorId, input.getCreatorId());
        //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getNameLike())) {
            queryWrapper = queryWrapper.like(Room::getName, input.getNameLike());
        }
        if (Extension.isNotNullOrEmpty(input.getAddressLike())) {
            queryWrapper = queryWrapper.like(Room::getAddress, input.getAddressLike());
        }
        if (Extension.isNotNullOrEmpty(input.getContentLike())) {
            queryWrapper = queryWrapper.like(Room::getContent, input.getContentLike());
        }
        return queryWrapper;
    }

    /**
     * 处理自习室对于的外键数据
     */
    private List<RoomDto> DispatchItem(List<RoomDto> items) throws InvocationTargetException, IllegalAccessException {

        for (RoomDto item : items) {
            //查询出关联的创建用户信息

            AppUser CreatorAppUserEntity = _AppUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getId, item.getCreatorId())).stream().findFirst().orElse(new AppUser());
            item.setCreatorAppUserDto(CreatorAppUserEntity.MapToDto());

            Long seatCount = _SeatMapper.selectCount(Wrappers.<Seat>lambdaQuery().eq(Seat::getRoomId, item.getId()));
            item.setSeatCount(seatCount.intValue());

            List<AppointRecord> appointRecords = _AppointRecordMapper.selectList(Wrappers.<AppointRecord>lambdaQuery().eq(AppointRecord::getRoomId, item.getId()));

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
     * 自习室分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<RoomDto> List(RoomPagedInput input) {
        //构建where条件+排序
        LambdaQueryWrapper<Room> queryWrapper = BuilderQuery(input);

        //按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(Room::getCreationTime);
        //构建一个分页查询的model
        Page<Room> page = new Page<>(input.getPage(), input.getLimit());
        //从数据库进行分页查询获取自习室数据
        IPage<Room> pageRecords = _RoomMpper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount = _RoomMpper.selectCount(queryWrapper);
        //把Room实体转换成Room传输模型
        List<RoomDto> items = Extension.copyBeanList(pageRecords.getRecords(), RoomDto.class);
        //计算表的数据
        items = DispatchItem(items);


        //返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个自习室查询
     */
    @SneakyThrows
    @Override
    public RoomDto Get(RoomPagedInput input) {
        if (input.getId() == null) {
            return new RoomDto();
        }
        PagedResult<RoomDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new RoomDto());
    }

    /**
     * 自习室创建或者修改
     */
    @SneakyThrows
    @Override
    public RoomDto CreateOrEdit(RoomDto input) {
        //声明一个自习室实体
        Room Room = input.MapToEntity();
        //调用数据库的增加或者修改方法
        saveOrUpdate(Room);
        //把传输模型返回给前端
        return Room.MapToDto();
    }

    /**
     * 自习室删除
     */
    @Override
    public void Delete(IdInput input) {
        Room entity = _RoomMpper.selectById(input.getId());
        _RoomMpper.deleteById(entity);
    }

    /**
     * 批量删除自习室
     *
     * @param input 包含所有要删除的自习室ID的对象
     */
    @Override
    public void BatchDelete(IdsInput input) {
        // 遍历输入的ID集合，对每个ID执行删除操作
        for (Integer id : input.getIds()) {
            // 创建一个新的IdInput对象，为当前ID设置删除参数
            IdInput idInput = new IdInput();
            idInput.setId(id);
            // 调用Delete方法，传入封装好的ID参数，执行删除操作
            Delete(idInput);
        }
    }
}
