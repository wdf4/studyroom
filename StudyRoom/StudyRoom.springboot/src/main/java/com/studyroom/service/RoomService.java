package com.studyroom.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studyroom.dto.*;
import com.studyroom.dto.query.*;
import com.studyroom.entity.*;
import com.studyroom.tools.dto.*;

/**
 * 自习室功能的Service接口的定义清单
 */
public interface RoomService extends IService<Room> {

    /**
     * 自习室的分页查询方法接口定义
     */
    public PagedResult<RoomDto> List(RoomPagedInput input) ;
    /**
     * 自习室的新增或者修改方法接口定义
     */
    public RoomDto CreateOrEdit(RoomDto input);

     /**
     * 获取自习室信息
     */
    public RoomDto Get(RoomPagedInput input);
 	 /**
     * 自习室删除
     */
    public void Delete(IdInput input);

    /**
     * 自习室批量删除
     */
    public void BatchDelete(IdsInput input);


}
