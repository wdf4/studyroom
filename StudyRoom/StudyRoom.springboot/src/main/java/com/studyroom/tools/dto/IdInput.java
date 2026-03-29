package com.studyroom.tools.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IdInput {
    @JsonProperty("Id")
    private Integer Id;
}
