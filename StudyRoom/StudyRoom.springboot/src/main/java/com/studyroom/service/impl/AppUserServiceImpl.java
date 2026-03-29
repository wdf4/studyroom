package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyroom.SysConst;
import com.studyroom.dto.AppUserDto;
import com.studyroom.dto.query.AppUserPagedInput;
import com.studyroom.entity.AppUser;
import com.studyroom.mapper.AppUserMapper;
import com.studyroom.service.AppUserService;
import com.studyroom.tools.Extension;
import com.studyroom.tools.JWTUtils;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import com.studyroom.tools.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户功能实现类
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {


    /**
     * 操作数据库的用户表mapper对象
     */
    @Autowired
    private AppUserMapper _AppUserMpper;

    @Override
    public PagedResult<AppUserDto> List(AppUserPagedInput input) {

        //select * from appuser where  username='admin' or 1=1

        //声明一个支持用户查询的(拉姆达)表达式
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.<AppUser>lambdaQuery()
                .eq(input.getId() != null, AppUser::getId, input.getId())
                .eq(input.getCreatorId() != null, AppUser::getCreatorId, input.getCreatorId());

        //如果前端搜索传入Name不为空,则进行精确查询
        if (Extension.isNotNullOrEmpty(input.getName())) {
            queryWrapper = queryWrapper.eq(AppUser::getName, input.getName());
        }

        //如果前端搜索传入Email不为空,则进行精确查询
        if (Extension.isNotNullOrEmpty(input.getEmail())) {
            queryWrapper = queryWrapper.eq(AppUser::getEmail, input.getEmail());
        }

        //如果前端搜索传入getPhoneNumber不为空,则进行精确查询
        if (Extension.isNotNullOrEmpty(input.getPhoneNumber())) {
            queryWrapper = queryWrapper.eq(AppUser::getPhoneNumber, input.getPhoneNumber());
        }

        //如果前端搜索传入getRoleType不为空,则进行精确查询
        if (input.getRoleType() != null) {
            queryWrapper = queryWrapper.eq(AppUser::getRoleType, input.getRoleType());
        }


        //按创建时间从大到小排序 最新的显示在最前面
        queryWrapper = queryWrapper.orderByDesc(AppUser::getCreationTime);
        //构建一个分页查询的model
        Page<AppUser> page = new Page<>(input.getPage(), input.getLimit());

        //从数据库进行分页查询获取用户数据
        IPage<AppUser> pageRecords = _AppUserMpper.selectPage(page, queryWrapper);

        //获取所有满足条件的数据行数
        Long totalCount = _AppUserMpper.selectCount(queryWrapper);

        //把用户实体转换成用户传输模型
        List<AppUserDto> items = Extension.copyBeanList(pageRecords.getRecords(), AppUserDto.class);

        //返回一个分页结构给前端
        return PagedResult.GetInstance(items, totalCount);
    }

    /**
     * 用户创建或者修改
     */
    @SneakyThrows
    @Override
    public AppUserDto CreateOrEdit(AppUserDto input) {

        if (Extension.isNullOrEmpty(input.getUserName())) {
            throw new CustomException("用户名不能为空");
        }

        AppUser appUser = input.MapToEntity();
        //调用数据库的增加或者修改方法
        saveOrUpdate(appUser);
        //把传输模型返回给前端
        return appUser.MapToDto();
    }

    /**
     * 用户删除
     */
    @Override
    public void Delete(IdInput input) {
        AppUser entity = _AppUserMpper.selectById(input.getId());
        _AppUserMpper.deleteById(entity);
    }

    /**
     * 用户批量删除
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
     * 用户单个查询
     */
    @Override
    public AppUserDto Get(AppUserPagedInput input) {
        if (input.getId() == null) {
            return new AppUserDto();
        }
        return List(input).getItems().stream().findFirst().orElse(new AppUserDto());
    }

    /**
     * 登录
     */
    public String SignIn(AppUserDto input) {
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUserName()), AppUser::getUserName, input.getUserName())
                .eq(Extension.isNotNullOrEmpty(input.getPassword()), AppUser::getPassword, input.getPassword())
                .eq(input.getRoleType() != null, AppUser::getRoleType, input.getRoleType());


        List<AppUser> items = _AppUserMpper.selectList(queryWrapper);
        if (items.stream().count() == 0) {
            throw new CustomException("请检查登录的账号或者密码,角色是否都正确!");
        }
        Map<String, String> map = new HashMap<>();
        map.put(SysConst.UserIdClaim, items.get(0).getId().toString());
        map.put(SysConst.RoleTypeClaim, items.get(0).getRoleType().toString());
        String token = JWTUtils.getToken(map);
        return token;
    }

    /**
     * 注册
     */
    @Override
    public AppUserDto Register(AppUserDto input) {


        //检查用户名是否存在
        Long userCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUserName()), AppUser::getUserName, input.getUserName()));
        if (userCount > 0) {
            throw new CustomException("该用户名已经存在!");
        }
        //检查邮箱是否存在
        Long emailCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUserName()), AppUser::getEmail, input.getEmail()));
        if (emailCount > 0) {
            throw new CustomException("该邮箱已经存在!");
        }
        //检查手机号是否存在
        Long phoneCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getPhoneNumber()), AppUser::getPhoneNumber, input.getPhoneNumber()));
        if (phoneCount > 0) {
            throw new CustomException("该手机号已经存在!");
        }
        return CreateOrEdit(input);
    }

    /**
     * 用户导出
     */
    @Override
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();


        AppUserPagedInput input = mapper.readValue(query, AppUserPagedInput.class);

        List<AppUserDto> items = List(input).getItems();


        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"用户表"
        HSSFSheet sheet = workbook.createSheet("用户表");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);
        //创建标题的显示样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //表头数据
        String[] header = {"账户", "密码", "姓名", "邮箱", "手机号码", "用户角色", "出生年月",};
        //遍历添加表头(下面模拟遍历用户，也是同样的操作过程)
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }


        for (int i = 0; i < items.size(); i++) {

            AppUserDto appUser = items.get(i);

            //创建一行
            HSSFRow row = sheet.createRow(i + 1);

            if (appUser.getUserName() != null) {
                row.createCell(0).setCellValue(new HSSFRichTextString(appUser.getUserName()));
            }
            if (appUser.getPassword() != null) {
                row.createCell(1).setCellValue(new HSSFRichTextString(appUser.getPassword()));
            }
            if (appUser.getName() != null) {
                row.createCell(2).setCellValue(new HSSFRichTextString(appUser.getName()));
            }
            if (appUser.getEmail() != null) {
                row.createCell(3).setCellValue(new HSSFRichTextString(appUser.getEmail()));
            }
            if (appUser.getPhoneNumber() != null) {
                row.createCell(4).setCellValue(new HSSFRichTextString(appUser.getPhoneNumber()));
            }
            if (appUser.getRoleType() != null) {
                row.createCell(5).setCellValue(new HSSFRichTextString(appUser.RoleTypeFormat()));
            }
            if (appUser.getBirth() != null) {
                row.createCell(6).setCellValue(new HSSFRichTextString(Extension.LocalDateTimeConvertString(appUser.getBirth(), null)));
            }
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

}
