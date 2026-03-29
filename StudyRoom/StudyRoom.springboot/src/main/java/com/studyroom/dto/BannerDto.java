package com.studyroom.dto;

import com.studyroom.enums.*;
import com.studyroom.tools.dto.BaseDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.studyroom.entity.*;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 封面类
 */
@Data
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
