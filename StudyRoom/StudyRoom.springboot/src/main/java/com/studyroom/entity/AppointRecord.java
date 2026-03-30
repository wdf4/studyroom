package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.studyroom.dto.AppointRecordDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * 预约记录表
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("`AppointRecord`")
public class AppointRecord extends BaseEntity {


    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    @TableField(value = "RoomId", updateStrategy = FieldStrategy.ALWAYS)
    private Integer RoomId;

    /**
     * 座位
     */
    @JsonProperty("SeatId")
    @TableField(value = "SeatId", updateStrategy = FieldStrategy.ALWAYS)
    private Integer SeatId;

    /**
     * 预约人
     */
    @JsonProperty("UserId")
    @TableField(value = "UserId", updateStrategy = FieldStrategy.ALWAYS)
    private Integer UserId;

    /**
     * 流水编号
     */
    @JsonProperty("No")
    @TableField(value = "No", updateStrategy = FieldStrategy.ALWAYS)
    private String No;

    /**
     * 手机号
     */
    @JsonProperty("Phone")
    @TableField(value = "Phone", updateStrategy = FieldStrategy.ALWAYS)
    private String Phone;

    /**
     * 姓名
     */
    @JsonProperty("Name")
    @TableField(value = "Name", updateStrategy = FieldStrategy.ALWAYS)
    private String Name;

    /**
     * 预约时间段范围类型
     */
    @JsonProperty("AppointDateType")
    @TableField(value = "AppointDateType", updateStrategy = FieldStrategy.ALWAYS)
    private Integer AppointDateType;

    /**
     * 预约日期
     */
    @JsonProperty("AppointDate")
    @TableField(value = "AppointDate", updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime AppointDate;

    /**
     * 起始时间
     */
    @JsonProperty("BeginTime")
    @TableField(value = "BeginTime", updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime BeginTime;

    /**
     * 截至时间
     */
    @JsonProperty("EndTime")
    @TableField(value = "EndTime", updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime EndTime;

    /**
     * 评分
     */
    @JsonProperty("CommentScore")
    @TableField(value = "CommentScore", updateStrategy = FieldStrategy.ALWAYS)
    private Double CommentScore;

    /**
     * 评论
     */
    @JsonProperty("Comment")
    @TableField(value = "SComment", updateStrategy = FieldStrategy.ALWAYS)
    private String Comment;

    /**
     * 预约状态
     */
    @JsonProperty("AppointStatus")
    @TableField(value = "AppointStatus", updateStrategy = FieldStrategy.ALWAYS)
    private Integer AppointStatus;

    /**
     * 把预约记录实体转换成预约记录传输模型
     */
    public AppointRecordDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        AppointRecordDto AppointRecordDto = new AppointRecordDto();
        BeanUtils.copyProperties(AppointRecordDto, this);
        return AppointRecordDto;
    }

}
