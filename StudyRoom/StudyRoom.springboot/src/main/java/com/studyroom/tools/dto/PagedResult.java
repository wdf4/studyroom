package com.studyroom.tools.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PagedResult<T> {

    private Long TotalCount;


    private List<T> Items;


    public static  PagedResult GetInstance(List items,Long totalCount)
    {
        PagedResult tPagedReuslt = new PagedResult<>();
        tPagedReuslt.setItems(items);
        tPagedReuslt.setTotalCount(totalCount);
        return tPagedReuslt;
    }



}
