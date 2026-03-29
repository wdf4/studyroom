package com.studyroom.dto;

import com.studyroom.entity.Room;
import com.studyroom.tools.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 自习室类
 */
@Data
public class RoomDto extends BaseDto {


    /**
     * 名称
     */
    @JsonProperty("Name")
    private String Name;


    /**
     * 封面
     */
    @JsonProperty("Cover")
    private String Cover;


    /**
     * 地址
     */
    @JsonProperty("Address")
    private String Address;


    /**
     * 介绍
     */
    @JsonProperty("Content")
    private String Content;


    /**
     * 座位个数
     */
    @JsonProperty("SeatCount")
    private Integer SeatCount;

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
     * 每月可取消次数
     */
    @JsonProperty("EveryMonCancelCount")
    private Integer EveryMonCancelCount;

    /**
     * 把自习室传输模型转换成自习室实体
     */
    public Room MapToEntity() throws InvocationTargetException, IllegalAccessException {
        Room Room = new Room();
        BeanUtils.copyProperties(Room, this);
        return Room;
    }

}
