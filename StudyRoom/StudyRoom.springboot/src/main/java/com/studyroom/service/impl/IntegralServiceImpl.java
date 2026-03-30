package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyroom.dto.IntegralDto;
import com.studyroom.dto.MyIntegralDataDto;
import com.studyroom.dto.query.IntegralPagedInput;
import com.studyroom.entity.AppUser;
import com.studyroom.entity.Integral;
import com.studyroom.mapper.AppUserMapper;
import com.studyroom.mapper.IntegralMapper;
import com.studyroom.service.IntegralService;
import com.studyroom.tools.BaseContext;
import com.studyroom.tools.Extension;
import com.studyroom.tools.dto.CurrentUserDto;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import com.studyroom.tools.exception.CustomException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 积分功能实现类
 */
@Slf4j
@Service
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, Integral> implements IntegralService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper _AppUserMapper;
    /**
     * 操作数据库的Integral表mapper对象
     */
    @Autowired
    private IntegralMapper _IntegralMpper;


    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<Integral> BuilderQuery(IntegralPagedInput input) {
        //声明一个支持积分查询的(拉姆达)表达式
        LambdaQueryWrapper<Integral> queryWrapper = Wrappers.<Integral>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, Integral::getId, input.getId())
                .eq(input.getCreatorId() != null, Integral::getCreatorId, input.getCreatorId());
        //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getTitleLike())) {
            queryWrapper = queryWrapper.like(Integral::getTitle, input.getTitleLike());
        }
        if (Extension.isNotNullOrEmpty(input.getSourceLike())) {
            queryWrapper = queryWrapper.like(Integral::getSource, input.getSourceLike());
        }
        if (Extension.isNotNullOrEmpty(input.getRelativeCodeLike())) {
            queryWrapper = queryWrapper.like(Integral::getRelativeCode, input.getRelativeCodeLike());
        }

        if (input.getUserId() != null) {
            queryWrapper = queryWrapper.eq(Integral::getUserId, input.getUserId());
        }
        return queryWrapper;
    }

    /**
     * 处理积分对于的外键数据
     */
    private List<IntegralDto> DispatchItem(List<IntegralDto> items) throws InvocationTargetException, IllegalAccessException {

        for (IntegralDto item : items) {
            //查询出关联的创建用户信息

            AppUser CreatorAppUserEntity = _AppUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getId, item.getCreatorId())).stream().findFirst().orElse(new AppUser());
            item.setCreatorAppUserDto(CreatorAppUserEntity.MapToDto());

            //查询出关联的AppUser表信息
            AppUser UserEntity = _AppUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getId, item.getUserId())).stream().findFirst().orElse(new AppUser());
            item.setUserDto(UserEntity.MapToDto());
        }
        return items;
    }

    /**
     * 积分分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<IntegralDto> List(IntegralPagedInput input) {
        //构建where条件+排序
        LambdaQueryWrapper<Integral> queryWrapper = BuilderQuery(input);

        //按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(Integral::getCreationTime);
        //构建一个分页查询的model
        Page<Integral> page = new Page<>(input.getPage(), input.getLimit());
        //从数据库进行分页查询获取积分数据
        IPage<Integral> pageRecords = _IntegralMpper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount = _IntegralMpper.selectCount(queryWrapper);
        //把Integral实体转换成Integral传输模型
        List<IntegralDto> items = Extension.copyBeanList(pageRecords.getRecords(), IntegralDto.class);
        //计算表的数据
        items = DispatchItem(items);


        //返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个积分查询
     */
    @SneakyThrows
    @Override
    public IntegralDto Get(IntegralPagedInput input) {
        if (input.getId() == null) {
            return new IntegralDto();
        }
        PagedResult<IntegralDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new IntegralDto());
    }

    /**
     * 积分创建或者修改
     */
    @SneakyThrows
    @Override
    public IntegralDto CreateOrEdit(IntegralDto input) {
        //声明一个积分实体
        Integral Integral = input.MapToEntity();
        //调用数据库的增加或者修改方法
        saveOrUpdate(Integral);
        //把传输模型返回给前端
        return Integral.MapToDto();
    }

    /**
     * 积分删除
     */
    @Override
    public void Delete(IdInput input) {
        Integral entity = _IntegralMpper.selectById(input.getId());
        _IntegralMpper.deleteById(entity);
    }

    /**
     * 积分批量删除
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
     * 得到我的积分数据
     */
    @Override
    public MyIntegralDataDto GetMyIntegralData() {
        MyIntegralDataDto myIntegralDataDto = new MyIntegralDataDto();
        CurrentUserDto currentUserDto = BaseContext.getCurrentUserDto();
        Integer userId = currentUserDto.getUserId();
        //得到总积分
        double totalIntegral = _IntegralMpper.selectList(Wrappers.<Integral>lambdaQuery().eq(Integral::getUserId, userId)).stream().mapToDouble(x -> x.getIntegralValue()).sum();

        myIntegralDataDto.setTotalIntegral(totalIntegral);
        return myIntegralDataDto;
    }

    /**
     * 清空逾期次数
     */
    @Transactional
    @Override
    public void OverdueTimesClear() {
        CurrentUserDto currentUserDto = BaseContext.getCurrentUserDto();
        Integer userId = currentUserDto.getUserId();
        //清空逾期次数需要2个积分
        AppUser appUser = _AppUserMapper.selectById(userId);

        if (Extension.isNullOrZero(appUser.getOverdueTimes())) {
            throw new CustomException("您没有逾期次数，无法清空");
        }
        //得到总积分
        double totalIntegral = _IntegralMpper.selectList(Wrappers.<Integral>lambdaQuery().eq(Integral::getUserId, userId)).stream().mapToDouble(x -> x.getIntegralValue()).sum();

        //清除需要的积分数
        int needIntegral = appUser.getOverdueTimes() * 2;


        if (needIntegral > totalIntegral) {
            throw new CustomException("您的积分不足,你还需要" + Math.abs(needIntegral - totalIntegral) + "积分");
        }
        appUser.setOverdueTimes(0);
        _AppUserMapper.updateById(appUser);

        Integral integral = new Integral();

        integral.setIntegralValue(-needIntegral);
        integral.setSource("逾期次数清零");
        integral.setUserId(userId);
        integral.setTitle("逾期次数清零-" + needIntegral + "积分");
        saveOrUpdate(integral);

    }


    /**
     * 登录赠送积分
     */
    @Transactional
    @Override
    public void SignGiveIntegral() {
        CurrentUserDto currentUserDto = BaseContext.getCurrentUserDto();
        Integer userId = currentUserDto.getUserId();

        LocalDateTime now = LocalDate.now().atTime(0, 0, 0);

        Long selectedCount = _IntegralMpper.selectCount(Wrappers.<Integral>lambdaQuery()
                .eq(Integral::getUserId, userId)
                .eq(Integral::getSource, "每天登录赠送积分")
                .ge(Integral::getCreationTime, now));
        if (selectedCount > 0) {
            return;
        }

        Integral integral = new Integral();
        integral.setIntegralValue(2);
        integral.setSource("每天登录赠送积分");
        integral.setUserId(userId);
        integral.setTitle("每天登录赠送积分+" + 2 + "积分");
        saveOrUpdate(integral);

    }

    /**
     * 统计最近30天积分的消耗量和获取量折线图
     */
    @SneakyThrows
    @Override
    public List<Object> GetIntegralConsumeAndGainChart() {
        List<Object> resultList = new ArrayList<>();
        //得到今天的日期
        LocalDateTime now = LocalDate.now().atTime(0, 0, 0);
        //循环30次

        List<Integral> integrals = _IntegralMpper.selectList(null);

        for (int i = 0; i < 30; i++) {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            LocalDateTime beginTime = now.minusDays(i).minusSeconds(1);
            LocalDateTime endTime = beginTime.plusDays(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
            String format = beginTime.format(formatter);

            stringObjectHashMap.put("Date", format);

            //先按照时间进行筛选
            List<Integral> filterDateList = integrals.stream().filter(x -> x.getCreationTime().isAfter(beginTime) && x.getCreationTime().isBefore(endTime)).toList();

            //得到积分>0的数据综合
            double gainIntegral = filterDateList.stream().filter(x -> x.getIntegralValue() > 0).mapToDouble(x -> x.getIntegralValue()).sum();
            //得到积分<0的数据综合
            double useIntegral = filterDateList.stream().filter(x -> x.getIntegralValue() < 0).mapToDouble(x -> x.getIntegralValue()).sum();

            stringObjectHashMap.put("GainIntegral", gainIntegral);
            stringObjectHashMap.put("UseIntegral", Math.abs(useIntegral));

            resultList.add(stringObjectHashMap);
        }
        //对结果进行反转
        Collections.reverse(resultList);
        return resultList;


    }

    /**
     * 系统整点总送积分
     */
    @Override
    public void MockIntegralValue() {
        //得到今天的日期
        LocalDateTime now = LocalDate.now().atTime(0, 0, 0);


        List<AppUser> appUsers = _AppUserMapper.selectList(null);

        var list = new ArrayList<Integral>();
        for (int i = 0; i < 30; i++) {
            LocalDateTime beginTime = now.minusDays(i);
            for (AppUser appUser : appUsers) {
                Integral integral = new Integral();
                integral.setUserId(appUser.getId());
                //随机1到10 整数
                integral.setIntegralValue(new Random().nextInt(10) + 1);
                integral.setTitle("系统整点在线赠送积分+" + integral.getIntegralValue());
                integral.setSource("系统整点在线赠送积分");
                integral.setCreationTime(beginTime);
                list.add(integral);
            }

        }
        _IntegralMpper.insert(list);
    }


}
