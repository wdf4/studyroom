package com.studyroom.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 统计各个自习室预约的状态信息请求参数
 */
@Data
public class AppointRoomAppointStatusDataQueryInput {
    /**
     * 起始时间
     */
    @JsonProperty("BeginDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime BeginDate;
    /**
     * 截至时间
     */
    @JsonProperty("EndDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime EndDate;
}
