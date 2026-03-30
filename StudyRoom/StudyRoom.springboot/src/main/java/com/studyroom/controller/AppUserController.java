package com.studyroom.controller;

import com.studyroom.dto.AppUserDto;
import com.studyroom.dto.query.AppUserPagedInput;
import com.studyroom.service.AppUserService;
import com.studyroom.service.IntegralService;
import com.studyroom.tools.BaseContext;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import com.studyroom.tools.dto.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * 用户控制器
 */
@RestController()
@RequestMapping("/User")
public class AppUserController {
    @Autowired()
    private AppUserService _AppUserService;

    @Autowired()
    private IntegralService _IntegralService;

    /**
     * 用户分页查询接口
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<AppUserDto> List(@RequestBody AppUserPagedInput input) {

        return _AppUserService.List(input);
    }

    /**
     * 用户创建或则修改接口
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public AppUserDto CreateOrEdit(@RequestBody AppUserDto input) {
        return _AppUserService.CreateOrEdit(input);

    }

    /**
     * 用户删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input) {
        _AppUserService.Delete(input);
    }

    /**
     * 用户批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input) {
        _AppUserService.BatchDelete(input);
    }


    /**
     * 查询单个对用户
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    public AppUserDto Get(@RequestBody AppUserPagedInput input) {
        return _AppUserService.Get(input);
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/SignIn", method = RequestMethod.POST)
    public ResponseData<String> SignIn(@RequestBody @Valid AppUserDto input, HttpServletRequest request) {
        String token = _AppUserService.SignIn(input);
        return ResponseData.<String>GetResponseDataInstance(token, "登录成功", true);
    }

    /**
     * 通过令牌获取用户信息
     *
     * 该方法通过用户令牌（Authorization头中的令牌）来获取当前用户的信息
     * 它首先确定当前用户，然后通过调用服务来获取用户的具体数据如果用户存在，
     * 系统将自动为用户签到并给予相应的积分
     *
     * @param token 用户的令牌，用于识别用户身份
     * @return 包含用户信息的AppUserDto对象
     */
    @SneakyThrows
    @RequestMapping(value = "/GetByToken", method = RequestMethod.POST)
    public AppUserDto GetByToken(@RequestHeader("Authorization") String token) {

        // 获取当前用户ID
        Integer userId = BaseContext.getCurrentUserDto().getUserId();

        // 初始化查询条件对象，并设置当前用户ID
        AppUserPagedInput queryInput = new AppUserPagedInput();
        queryInput.setId(userId);

        // 从用户服务中获取用户信息
        AppUserDto AppUserDto = _AppUserService.Get(queryInput);

        // 如果用户存在，则执行签到积分操作
        if (AppUserDto.getId() > 0) {
            _IntegralService.SignGiveIntegral();
        }

        // 返回获取到的用户信息
        return AppUserDto;
    }

    /**
     * 用户注册接口
     */
    @RequestMapping(value = "/Register", method = RequestMethod.POST)
    public AppUserDto Register(@RequestBody @Valid AppUserDto input) throws Exception {
        return _AppUserService.Register(input);
    }

    /**
     * 修改密码接口
     */
    @RequestMapping(value = "/ChangePassword", method = RequestMethod.POST)
    public ResponseData<Void> ChangePassword(@RequestBody AppUserDto input) {
        _AppUserService.ChangePassword(input);
        return ResponseData.<Void>GetResponseDataInstance(null, "密码修改成功", true);
    }

    /**
     * 用户导出
     */
    @RequestMapping(value = "/Export", method = RequestMethod.GET)
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {
        _AppUserService.Export(query, response);
    }
}
