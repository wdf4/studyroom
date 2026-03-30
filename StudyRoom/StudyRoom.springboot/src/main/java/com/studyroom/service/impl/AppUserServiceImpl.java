package com.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户功能实现类
 */
@Slf4j
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private AppUserMapper _AppUserMpper;

    @Override
    public PagedResult<AppUserDto> List(AppUserPagedInput input) {
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.<AppUser>lambdaQuery()
                .eq(input.getId() != null, AppUser::getId, input.getId())
                .eq(input.getCreatorId() != null, AppUser::getCreatorId, input.getCreatorId());

        if (Extension.isNotNullOrEmpty(input.getName())) {
            queryWrapper = queryWrapper.eq(AppUser::getName, input.getName());
        }
        if (Extension.isNotNullOrEmpty(input.getEmail())) {
            queryWrapper = queryWrapper.eq(AppUser::getEmail, input.getEmail());
        }
        if (Extension.isNotNullOrEmpty(input.getPhoneNumber())) {
            queryWrapper = queryWrapper.eq(AppUser::getPhoneNumber, input.getPhoneNumber());
        }
        if (input.getRoleType() != null) {
            queryWrapper = queryWrapper.eq(AppUser::getRoleType, input.getRoleType());
        }

        queryWrapper = queryWrapper.orderByDesc(AppUser::getCreationTime);
        Page<AppUser> page = new Page<>(input.getPage(), input.getLimit());
        IPage<AppUser> pageRecords = _AppUserMpper.selectPage(page, queryWrapper);
        Long totalCount = _AppUserMpper.selectCount(queryWrapper);
        List<AppUserDto> items = Extension.copyBeanList(pageRecords.getRecords(), AppUserDto.class);
        return PagedResult.GetInstance(items, totalCount);
    }

    /**
     * 用户创建或者修改（管理员操作）
     */
    @SneakyThrows
    @Override
    public AppUserDto CreateOrEdit(AppUserDto input) {
        if (Extension.isNullOrEmpty(input.getUserName())) {
            throw new CustomException("用户名不能为空");
        }
        // 有密码且不是 BCrypt 格式时才加密（避免编辑时重复加密）
        if (Extension.isNotNullOrEmpty(input.getPassword()) && !input.getPassword().startsWith("$2a$")) {
            input.setPassword(PASSWORD_ENCODER.encode(input.getPassword()));
        }
        AppUser appUser = input.MapToEntity();
        saveOrUpdate(appUser);
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
        // 先按用户名和角色查询，再校验密码（BCrypt 无法直接 SQL 比对）
        LambdaQueryWrapper<AppUser> queryWrapper = Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUserName()), AppUser::getUserName, input.getUserName())
                .eq(input.getRoleType() != null, AppUser::getRoleType, input.getRoleType());

        List<AppUser> items = _AppUserMpper.selectList(queryWrapper);
        if (items.isEmpty()) {
            throw new CustomException("请检查登录的账号或者密码,角色是否都正确!");
        }
        AppUser user = items.get(0);
        if (!PASSWORD_ENCODER.matches(input.getPassword(), user.getPassword())) {
            throw new CustomException("请检查登录的账号或者密码,角色是否都正确!");
        }
        Map<String, String> map = new HashMap<>();
        map.put(SysConst.UserIdClaim, user.getId().toString());
        map.put(SysConst.RoleTypeClaim, user.getRoleType().toString());
        return JWTUtils.getToken(map);
    }

    /**
     * 注册
     */
    @Override
    public AppUserDto Register(AppUserDto input) {
        Long userCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getUserName()), AppUser::getUserName, input.getUserName()));
        if (userCount > 0) {
            throw new CustomException("该用户名已经存在!");
        }
        Long emailCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getEmail()), AppUser::getEmail, input.getEmail()));
        if (emailCount > 0) {
            throw new CustomException("该邮箱已经存在!");
        }
        Long phoneCount = _AppUserMpper.selectCount(Wrappers.<AppUser>lambdaQuery()
                .eq(Extension.isNotNullOrEmpty(input.getPhoneNumber()), AppUser::getPhoneNumber, input.getPhoneNumber()));
        if (phoneCount > 0) {
            throw new CustomException("该手机号已经存在!");
        }
        // 注册时加密密码
        if (Extension.isNotNullOrEmpty(input.getPassword())) {
            input.setPassword(PASSWORD_ENCODER.encode(input.getPassword()));
        }
        return CreateOrEdit(input);
    }

    /**
     * 修改密码（BCrypt 校验旧密码后更新）
     */
    @SneakyThrows
    @Override
    public void ChangePassword(AppUserDto input) {
        AppUser user = _AppUserMpper.selectById(input.getId());
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        if (!PASSWORD_ENCODER.matches(input.getOrginPassword(), user.getPassword())) {
            throw new CustomException("原始密码错误");
        }
        user.setPassword(PASSWORD_ENCODER.encode(input.getPassword()));
        _AppUserMpper.updateById(user);
    }

    /**
     * 用户导出（不导出密码字段）
     */
    @Override
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AppUserPagedInput input = mapper.readValue(query, AppUserPagedInput.class);
        List<AppUserDto> items = List(input).getItems();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("用户表");
        sheet.setDefaultColumnWidth(10);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFRow headrow = sheet.createRow(0);

        // 不导出密码列
        String[] header = {"账户", "姓名", "邮箱", "手机号码", "用户角色", "出生年月"};
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = headrow.createCell(i);
            cell.setCellValue(new HSSFRichTextString(header[i]));
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < items.size(); i++) {
            AppUserDto appUser = items.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            if (appUser.getUserName() != null) {
                row.createCell(0).setCellValue(new HSSFRichTextString(appUser.getUserName()));
            }
            if (appUser.getName() != null) {
                row.createCell(1).setCellValue(new HSSFRichTextString(appUser.getName()));
            }
            if (appUser.getEmail() != null) {
                row.createCell(2).setCellValue(new HSSFRichTextString(appUser.getEmail()));
            }
            if (appUser.getPhoneNumber() != null) {
                row.createCell(3).setCellValue(new HSSFRichTextString(appUser.getPhoneNumber()));
            }
            if (appUser.getRoleType() != null) {
                row.createCell(4).setCellValue(new HSSFRichTextString(appUser.RoleTypeFormat()));
            }
            if (appUser.getBirth() != null) {
                row.createCell(5).setCellValue(new HSSFRichTextString(Extension.LocalDateTimeConvertString(appUser.getBirth(), null)));
            }
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xls");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
