package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyroom.dto.BannerDto;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 封面表
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("`Banner`")
public class Banner extends BaseEntity {

    /**
     * 封面
     */
    @JsonProperty("Cover")
    @TableField(value = "Cover", updateStrategy = FieldStrategy.ALWAYS)
    private String Cover;

    /**
     * 备注
     */
    @JsonProperty("Remark")
    @TableField(value = "Remark", updateStrategy = FieldStrategy.ALWAYS)
    private String Remark;

    /**
     * 把封面实体转换成封面传输模型
     */
    public BannerDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        BannerDto bannerDto = new BannerDto();
        BeanUtils.copyProperties(bannerDto, this);
        return bannerDto;
    }
}
