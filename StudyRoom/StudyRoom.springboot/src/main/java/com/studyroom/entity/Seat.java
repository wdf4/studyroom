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
 * 座位表
 */
@Data
@TableName("`Seat`")
public class Seat extends BaseEntity {


    /**
     * 编号
     */
    @JsonProperty("No")
    @TableField(value = "No", updateStrategy = FieldStrategy.IGNORED)
    private String No;

    /**
     * 行
     */
    @JsonProperty("Row")
    @TableField(value = "SRow", updateStrategy = FieldStrategy.IGNORED)
    private Integer SRow;

    /**
     * 列
     */
    @JsonProperty("Col")
    @TableField(value = "SCol", updateStrategy = FieldStrategy.IGNORED)
    private Integer SCol;

    /**
     * 是否维修
     */
    @JsonProperty("IsMaintain")
    @TableField(value = "IsMaintain", updateStrategy = FieldStrategy.IGNORED)
    private Boolean IsMaintain;

    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    @TableField(value = "RoomId", updateStrategy = FieldStrategy.IGNORED)
    private Integer RoomId;

    /**
     * 把座位实体转换成座位传输模型
     */
    public SeatDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        SeatDto SeatDto = new SeatDto();
        BeanUtils.copyProperties(SeatDto, this);
        return SeatDto;
    }

}
