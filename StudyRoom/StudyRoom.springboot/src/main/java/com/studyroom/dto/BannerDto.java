package com.studyroom.dto;

import com.studyroom.tools.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyroom.entity.*;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

import lombok.Data;

/**
 * 封面类
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class BannerDto extends BaseDto {


    /**
     * 封面
     */
    @JsonProperty("Cover")
    private String Cover;


    /**
     * 备注
     */
    @JsonProperty("Remark")
    private String Remark;

    /**
     * 把封面传输模型转换成封面实体
     */
    public Banner MapToEntity() throws InvocationTargetException, IllegalAccessException {
        Banner Banner = new Banner();
        BeanUtils.copyProperties(Banner, this);
        return Banner;
    }

}
