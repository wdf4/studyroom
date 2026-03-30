package com.studyroom.dto.query;

import com.studyroom.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Integral查询模型
 */
@NoArgsConstructor
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class IntegralPagedInput extends PagedInput {

    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;
    /**
     * 标题模糊查询条件
     */
    @JsonProperty("TitleLike")
    private String TitleLike;
    /**
     * 来源模糊查询条件
     */
    @JsonProperty("SourceLike")
    private String SourceLike;
    /**
     * 关联号模糊查询条件
     */
    @JsonProperty("RelativeCodeLike")
    private String RelativeCodeLike;
    /**
     * 用户
     */
    @JsonProperty("UserId")
    private Integer UserId;

}
