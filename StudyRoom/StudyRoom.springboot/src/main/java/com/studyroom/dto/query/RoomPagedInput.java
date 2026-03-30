package com.studyroom.dto.query;

import com.studyroom.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Room查询模型
 */
@NoArgsConstructor
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class RoomPagedInput extends PagedInput {

    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;
    /**
     * 名称模糊查询条件
     */
    @JsonProperty("NameLike")
    private String NameLike;
    /**
     * 地址模糊查询条件
     */
    @JsonProperty("AddressLike")
    private String AddressLike;
    /**
     * 介绍模糊查询条件
     */
    @JsonProperty("ContentLike")
    private String ContentLike;

}
