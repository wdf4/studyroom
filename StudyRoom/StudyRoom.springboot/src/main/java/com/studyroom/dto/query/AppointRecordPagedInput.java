package com.studyroom.dto.query;

import com.studyroom.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AppointRecord查询模型
 */
@NoArgsConstructor
@Data
public class AppointRecordPagedInput extends PagedInput {

    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;
    /**
     * 流水编号模糊查询条件
     */
    @JsonProperty("NoLike")
    private String NoLike;
    /**
     * 手机号模糊查询条件
     */
    @JsonProperty("PhoneLike")
    private String PhoneLike;
    /**
     * 姓名模糊查询条件
     */
    @JsonProperty("NameLike")
    private String NameLike;
    /**
     * 评论模糊查询条件
     */
    @JsonProperty("CommentLike")
    private String CommentLike;
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
     * 预约状态
     */
    @JsonProperty("AppointStatus")
    private Integer AppointStatus;
    /**
     * 起始时间时间范围
     */
    @JsonProperty("BeginTimeRange")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> BeginTimeRange;
    /**
     * 截至时间时间范围
     */
    @JsonProperty("EndTimeRange")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> EndTimeRange;

}
