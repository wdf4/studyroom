package com.studyroom.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyroom.dto.SeatDto;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 座位表
 */
@Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("`Seat`")
public class Seat extends BaseEntity {

    /**
     * 编号
     */
    @JsonProperty("No")
    @TableField(value = "No", updateStrategy = FieldStrategy.ALWAYS)
    private String No;

    /**
     * 行
     */
    @JsonProperty("Row")
    @TableField(value = "SRow", updateStrategy = FieldStrategy.ALWAYS)
    private Integer SRow;

    /**
     * 列
     */
    @JsonProperty("Col")
    @TableField(value = "SCol", updateStrategy = FieldStrategy.ALWAYS)
    private Integer SCol;

    /**
     * 是否维修
     */
    @JsonProperty("IsMaintain")
    @TableField(value = "IsMaintain", updateStrategy = FieldStrategy.ALWAYS)
    private Boolean IsMaintain;

    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    @TableField(value = "RoomId", updateStrategy = FieldStrategy.ALWAYS)
    private Integer RoomId;

    /**
     * 把座位实体转换成座位传输模型
     */
    public SeatDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        SeatDto seatDto = new SeatDto();
        BeanUtils.copyProperties(seatDto, this);
        return seatDto;
    }
}
