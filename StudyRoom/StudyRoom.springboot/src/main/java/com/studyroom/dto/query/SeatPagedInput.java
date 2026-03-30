package com.studyroom.dto.query;

import com.studyroom.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Seat查询模型
 */
@NoArgsConstructor
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class SeatPagedInput extends PagedInput {

    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;
    /**
     * 编号模糊查询条件
     */
    @JsonProperty("NoLike")
    private String NoLike;
    /**
     * 自习室
     */
    @JsonProperty("RoomId")
    private Integer RoomId;
    /**
     * 是否维修
     */
    @JsonProperty("IsMaintain")
    private Boolean IsMaintain;


    /**
     * 选择的日期
     */
    @JsonProperty("SelectDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime SelectDate;

}
