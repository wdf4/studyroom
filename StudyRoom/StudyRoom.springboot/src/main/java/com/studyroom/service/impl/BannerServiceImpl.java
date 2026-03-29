package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyroom.dto.BannerDto;
import com.studyroom.dto.query.BannerPagedInput;
import com.studyroom.entity.AppUser;
import com.studyroom.entity.Banner;
import com.studyroom.mapper.AppUserMapper;
import com.studyroom.mapper.BannerMapper;
import com.studyroom.service.BannerService;
import com.studyroom.tools.Extension;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 封面功能实现类
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    /**
     * 操作数据库AppUser表mapper对象
     */
    @Autowired
    private AppUserMapper _AppUserMapper;
    /**
     * 操作数据库的Banner表mapper对象
     */
    @Autowired
    private BannerMapper _BannerMpper;


    /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<Banner> BuilderQuery(BannerPagedInput input) {
        //声明一个支持封面查询的(拉姆达)表达式
        LambdaQueryWrapper<Banner> queryWrapper = Wrappers.<Banner>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, Banner::getId, input.getId())
                .eq(input.getCreatorId() != null, Banner::getCreatorId, input.getCreatorId());
        //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getRemarkLike())) {
            queryWrapper = queryWrapper.like(Banner::getRemark, input.getRemarkLike());
        }
        return queryWrapper;
    }

    /**
     * 处理封面对于的外键数据
     */
    private List<BannerDto> DispatchItem(List<BannerDto> items) throws InvocationTargetException, IllegalAccessException {

        for (BannerDto item : items) {
            //查询出关联的创建用户信息

            AppUser CreatorAppUserEntity = _AppUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getId, item.getCreatorId())).stream().findFirst().orElse(new AppUser());
            item.setCreatorAppUserDto(CreatorAppUserEntity.MapToDto());
        }
        return items;
    }

    /**
     * 封面分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<BannerDto> List(BannerPagedInput input) {
        //构建where条件+排序
        LambdaQueryWrapper<Banner> queryWrapper = BuilderQuery(input);

        //按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(Banner::getCreationTime);
        //构建一个分页查询的model
        Page<Banner> page = new Page<>(input.getPage(), input.getLimit());
        //从数据库进行分页查询获取封面数据
        IPage<Banner> pageRecords = _BannerMpper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount = _BannerMpper.selectCount(queryWrapper);
        //把Banner实体转换成Banner传输模型
        List<BannerDto> items = Extension.copyBeanList(pageRecords.getRecords(), BannerDto.class);
        //计算表的数据
        items = DispatchItem(items);


        //返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);

    }

    /**
     * 单个封面查询
     */
    @SneakyThrows
    @Override
    public BannerDto Get(BannerPagedInput input) {
        if (input.getId() == null) {
            return new BannerDto();
        }
        PagedResult<BannerDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new BannerDto());
    }

    /**
     * 封面创建或者修改
     */
    @SneakyThrows
    @Override
    public BannerDto CreateOrEdit(BannerDto input) {
        //声明一个封面实体
        Banner Banner = input.MapToEntity();
        //调用数据库的增加或者修改方法
        saveOrUpdate(Banner);
        //把传输模型返回给前端
        return Banner.MapToDto();
    }

    /**
     * 封面删除
     */
    @Override
    public void Delete(IdInput input) {
        Banner entity = _BannerMpper.selectById(input.getId());
        _BannerMpper.deleteById(entity);
    }

    /**
     * 封面批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }
}
