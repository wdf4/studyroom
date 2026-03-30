package com.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.studyroom.entity.AppUser;
import com.studyroom.enums.RoleTypeEnum;
import com.studyroom.tools.dto.BaseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class AppUserDto extends BaseDto {

    /**
     * 账号
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间")
    @JsonProperty("UserName")
    private String UserName;

    /**
     * 密码
     */
    @JsonProperty("Password")
    private String Password;

    /**
     * 原始密码（修改密码时使用）
     */
    @JsonProperty("OrginPassword")
    private String OrginPassword;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
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
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {})
    @JsonProperty("PhoneNumber")
    private String PhoneNumber;

    /**
     * 出生年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("Birth")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime Birth;

    /**
     * 用户角色
     */
    @JsonProperty("RoleType")
    private Integer RoleType;

    @JsonProperty("RoleTypeFormat")
    public String RoleTypeFormat() {
        return RoleTypeEnum.GetEnum(RoleType).toString();
    }

    /**
     * 逾期次数
     */
    @JsonProperty("OverdueTimes")
    private Integer OverdueTimes;

    /**
     * 把用户传输模型转换成用户实体
     */
    public AppUser MapToEntity() throws InvocationTargetException, IllegalAccessException {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(appUser, this);
        return appUser;
    }
}
