package com.studyroom.tools.dto;

import lombok.Data;

@Data
public class PagedInput {



    private Long CreatorId;

    /**
     * 页大小
     */
    private Long Limit;

    /**
     * 页码
     */
    private Long Page;



    public PagedInput(){
        if(Limit==null||Limit==0){
            Limit=Long.valueOf(100);
        }
        if(Page==null||Page==0){
            Page=Long.valueOf(1);
        }
    }
}
