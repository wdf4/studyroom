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
 * 自习室表
 */
@Data
@TableName("`Room`")
public class Room extends BaseEntity {


    /**
     * 名称
     */
    @JsonProperty("Name")
    @TableField(value = "Name", updateStrategy = FieldStrategy.IGNORED)
    private String Name;

    /**
     * 封面
     */
    @JsonProperty("Cover")
    @TableField(value = "Cover", updateStrategy = FieldStrategy.IGNORED)
    private String Cover;

    /**
     * 地址
     */
    @JsonProperty("Address")
    @TableField(value = "Address", updateStrategy = FieldStrategy.IGNORED)
    private String Address;

    /**
     * 介绍
     */
    @JsonProperty("Content")
    @TableField(value = "Content", updateStrategy = FieldStrategy.IGNORED)
    private String Content;

    /**
     * 每月可取消次数
     */
    @JsonProperty("EveryMonCancelCount")
    @TableField(value = "EveryMonCancelCount", updateStrategy = FieldStrategy.IGNORED)
    private Integer EveryMonCancelCount;

    /**
     * 把自习室实体转换成自习室传输模型
     */
    public RoomDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        RoomDto RoomDto = new RoomDto();
        BeanUtils.copyProperties(RoomDto, this);
        return RoomDto;
    }

}
