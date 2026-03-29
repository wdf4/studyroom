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
 * 预约记录类
 */
@Data
public class AppointRecordDto extends BaseDto {


    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    private Integer RoomId;


    /**
     * 座位
     */
    @JsonProperty("SeatId")
    private Integer SeatId;


    /**
     * 预约人
     */
    @JsonProperty("UserId")
    private Integer UserId;


    /**
     * 流水编号
     */
    @JsonProperty("No")
    private String No;


    /**
     * 手机号
     */
    @JsonProperty("Phone")
    private String Phone;


    /**
     * 姓名
     */
    @JsonProperty("Name")
    private String Name;

    /**
     * 预约时间段范围类型
     */
    @JsonProperty("AppointDateType")
    private Integer AppointDateType;

    public String getAppointDateTypeFormat() {
        return AppointDateTypeEnum.GetEnum(AppointDateType).toString();
    }

    /**
     * 预约日期
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("AppointDate")
    private LocalDateTime AppointDate;


    /**
     * 起始时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("BeginTime")
    private LocalDateTime BeginTime;


    /**
     * 截至时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("EndTime")
    private LocalDateTime EndTime;


    /**
     * 评分
     */
    @JsonProperty("CommentScore")
    private Double CommentScore;


    /**
     * 评论
     */
    @JsonProperty("Comment")
    private String Comment;


    /**
     * 预约状态
     */
    @JsonProperty("AppointStatus")
    private Integer AppointStatus;

    public String getAppointStatusFormat() {
        return AppointStatusEnum.GetEnum(AppointStatus).toString();
    }

    private String AppointStatusFormat;


    @JsonProperty("UserDto")
    private AppUserDto UserDto;

    @JsonProperty("RoomDto")
    private RoomDto RoomDto;

    @JsonProperty("SeatDto")
    private SeatDto SeatDto;


    /**
     * 把预约记录传输模型转换成预约记录实体
     */
    public AppointRecord MapToEntity() throws InvocationTargetException, IllegalAccessException {
        AppointRecord AppointRecord = new AppointRecord();
        BeanUtils.copyProperties(AppointRecord, this);
        return AppointRecord;
    }

}
