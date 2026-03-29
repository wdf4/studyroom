package com.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SeatArrange {

    /**
     * 上午的座位
     */
    @JsonProperty("AmSeatDtoList")
    public List<List<SeatDto>> AmSeatDtoList;

    /**
     * 下午的座位
     */
    @JsonProperty("PmSeatDtoList")
    public List<List<SeatDto>> PmSeatDtoList;

    /**
     * 夜晚的座位
     */
    @JsonProperty("NmSeatDtoList")
    public List<List<SeatDto>> NmSeatDtoList;

}
