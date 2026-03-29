package com.studyroom.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointRoomUseRateQueryInput {


    /**
     * 选座的日期
     */
    @JsonProperty("SelectDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime SelectDate;
}
