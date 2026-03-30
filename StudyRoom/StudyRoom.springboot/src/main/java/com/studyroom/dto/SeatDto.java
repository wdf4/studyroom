package com.studyroom.dto;

import com.studyroom.tools.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyroom.entity.*;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

import lombok.Data;

/**
 * 座位类
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class SeatDto extends BaseDto {


    /**
     * 编号
     */
    @JsonProperty("No")
    private String No;


    /**
     * 行
     */
    @JsonProperty("Row")
    private Integer SRow;


    /**
     * 列
     */
    @JsonProperty("Col")
    private Integer SCol;


    /**
     * 是否维修
     */
    @JsonProperty("IsMaintain")
    private Boolean IsMaintain;


    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    private Integer RoomId;

    @JsonProperty("RoomDto")
    private RoomDto RoomDto;

    /**
     * 总评论个数
     */
    @JsonProperty("TotalCommentCount")
    private Integer TotalCommentCount;


    /**
     * 总使用次数
     */
    @JsonProperty("TotalUseCount")
    private Integer TotalUseCount;

    /**
     * 评分平均值
     */
    @JsonProperty("AgvCommentScore")
    private double AgvCommentScore;

    /**
     * 是否占用
     */
    @JsonProperty("IsOccupy")
    private Boolean IsOccupy;

    /**
     * 把座位传输模型转换成座位实体
     */
    public Seat MapToEntity() throws InvocationTargetException, IllegalAccessException {
        Seat Seat = new Seat();
        BeanUtils.copyProperties(Seat, this);
        return Seat;
    }

}
