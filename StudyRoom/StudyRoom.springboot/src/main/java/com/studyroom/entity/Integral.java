package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.studyroom.dto.IntegralDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 积分表
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("`Integral`")
public class Integral extends BaseEntity {


    /**
     * 标题
     */
    @JsonProperty("Title")
    @TableField(value = "Title", updateStrategy = FieldStrategy.ALWAYS)
    private String Title;

    /**
     * 用户
     */
    @JsonProperty("UserId")
    @TableField(value = "UserId", updateStrategy = FieldStrategy.ALWAYS)
    private Integer UserId;

    /**
     * 积分值
     */
    @JsonProperty("IntegralValue")
    @TableField(value = "IntegralValue", updateStrategy = FieldStrategy.ALWAYS)
    private Integer IntegralValue;

    /**
     * 来源
     */
    @JsonProperty("Source")
    @TableField(value = "Source", updateStrategy = FieldStrategy.ALWAYS)
    private String Source;

    /**
     * 关联号
     */
    @JsonProperty("RelativeCode")
    @TableField(value = "RelativeCode", updateStrategy = FieldStrategy.ALWAYS)
    private String RelativeCode;

    /**
     * 把积分实体转换成积分传输模型
     */
    public IntegralDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        IntegralDto IntegralDto = new IntegralDto();
        BeanUtils.copyProperties(IntegralDto, this);
        return IntegralDto;
    }

}
