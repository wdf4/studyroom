package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

import java.time.LocalDateTime;

import com.studyroom.dto.*;
import com.baomidou.mybatisplus.annotation.FieldStrategy;

/**
 * 封面表
 */
@Data
@TableName("`Banner`")
public class Banner extends BaseEntity {


    /**
     * 封面
     */
    @JsonProperty("Cover")
    @TableField(value = "Cover", updateStrategy = FieldStrategy.IGNORED)
    private String Cover;

    /**
     * 备注
     */
    @JsonProperty("Remark")
    @TableField(value = "Remark", updateStrategy = FieldStrategy.IGNORED)
    private String Remark;

    /**
     * 把封面实体转换成封面传输模型
     */
    public BannerDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        BannerDto BannerDto = new BannerDto();
        BeanUtils.copyProperties(BannerDto, this);
        return BannerDto;
    }

}
