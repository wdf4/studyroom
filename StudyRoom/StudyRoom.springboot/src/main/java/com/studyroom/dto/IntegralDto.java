package com.studyroom.dto;

import com.studyroom.entity.Integral;
import com.studyroom.tools.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 积分类
 */
@Data
public class IntegralDto extends BaseDto {


    /**
     * 标题
     */
    @JsonProperty("Title")
    private String Title;

    /**
     * 用户
     */
    @JsonProperty("UserId")
    private Integer UserId;

    /**
     * 积分值
     */
    @JsonProperty("IntegralValue")
    private Integer IntegralValue;

    /**
     * 来源
     */
    @JsonProperty("Source")
    private String Source;

    /**
     * 关联号
     */
    @JsonProperty("RelativeCode")
    private String RelativeCode;


    @JsonProperty("UserDto")
    private AppUserDto UserDto;
    /**
     * 把积分传输模型转换成积分实体
     */
    public Integral MapToEntity() throws InvocationTargetException, IllegalAccessException {
        Integral Integral = new Integral();
        BeanUtils.copyProperties(Integral, this);
        return Integral;
    }

}
