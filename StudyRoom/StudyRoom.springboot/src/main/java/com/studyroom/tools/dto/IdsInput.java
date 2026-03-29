package com.studyroom.tools.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class IdsInput {
    @JsonProperty("Ids")
    private ArrayList<Integer> Ids;
}
