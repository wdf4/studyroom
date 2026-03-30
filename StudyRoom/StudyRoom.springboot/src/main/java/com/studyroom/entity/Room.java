package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyroom.dto.RoomDto;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 自习室表
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("`Room`")
public class Room extends BaseEntity {

    /**
     * 名称
     */
    @JsonProperty("Name")
    @TableField(value = "Name", updateStrategy = FieldStrategy.ALWAYS)
    private String Name;

    /**
     * 封面
     */
    @JsonProperty("Cover")
    @TableField(value = "Cover", updateStrategy = FieldStrategy.ALWAYS)
    private String Cover;

    /**
     * 地址
     */
    @JsonProperty("Address")
    @TableField(value = "Address", updateStrategy = FieldStrategy.ALWAYS)
    private String Address;

    /**
     * 介绍
     */
    @JsonProperty("Content")
    @TableField(value = "Content", updateStrategy = FieldStrategy.ALWAYS)
    private String Content;

    /**
     * 每月可取消次数
     */
    @JsonProperty("EveryMonCancelCount")
    @TableField(value = "EveryMonCancelCount", updateStrategy = FieldStrategy.ALWAYS)
    private Integer EveryMonCancelCount;

    /**
     * 把自习室实体转换成自习室传输模型
     */
    public RoomDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(roomDto, this);
        return roomDto;
    }
}
