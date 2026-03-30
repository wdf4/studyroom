package com.studyroom.dto.query;

import com.studyroom.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Banner查询模型
 */
@NoArgsConstructor
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class BannerPagedInput extends PagedInput {

    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;
    /**
     * 备注模糊查询条件
     */
    @JsonProperty("RemarkLike")
    private String RemarkLike;

}
