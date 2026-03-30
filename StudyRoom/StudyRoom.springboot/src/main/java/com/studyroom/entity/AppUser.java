package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.studyroom.dto.AppUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("AppUser")
public class AppUser extends BaseEntity {

    /**
     * 账号
     */
    @JsonProperty("UserName")
    private String UserName;
    /**
     * 密码
     */
    @JsonProperty("Password")
    private String Password;

    /**
     * 邮箱
     */
    @JsonProperty("Email")
    private String Email;
    /**
     * 头像
     */
    @JsonProperty("ImageUrls")
    private String ImageUrls;
    /**
     * 名称
     */
    @JsonProperty("Name")
    private String Name;
    /**
     * 手机号码
     */
    @JsonProperty("PhoneNumber")
    private String PhoneNumber;
    /**
     * 出生年月
     */
    @JsonProperty("Birth")
    private LocalDateTime Birth;

    /**
     * 用户角色
     */
    @JsonProperty("RoleType")
    private Integer RoleType;

    /**
     * 逾期次数
     */
    @JsonProperty("OverdueTimes")
    private Integer OverdueTimes;


    /**
     * 把用户实体转换成用户传输模型
     */
    public AppUserDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        AppUserDto AppUserDto = new AppUserDto();
        BeanUtils.copyProperties(AppUserDto, this);
        return AppUserDto;
    }

}
