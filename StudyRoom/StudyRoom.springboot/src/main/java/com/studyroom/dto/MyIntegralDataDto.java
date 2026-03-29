package com.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MyIntegralDataDto {

    /**
     * 总积分
     */
    @JsonProperty("TotalIntegral")
    public Double TotalIntegral;
}
