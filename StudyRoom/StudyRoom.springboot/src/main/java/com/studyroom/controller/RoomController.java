package com.studyroom.controller;
import com.studyroom.dto.*;
import com.studyroom.dto.query.*;
import com.studyroom.service.*;
import com.studyroom.tools.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.SneakyThrows;

/**
 * 自习室控制器
 */
@RestController()
@RequestMapping("/Room")
public class RoomController {
    @Autowired()
    private  RoomService _RoomService;
    /**
     * 自习室分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<RoomDto> List(@RequestBody RoomPagedInput input)  {
        return _RoomService.List(input);
    }
     /**
     * 单个自习室查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public RoomDto Get(@RequestBody RoomPagedInput input) {

        return _RoomService.Get(input);
    }

    /**
     * 自习室创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public RoomDto CreateOrEdit(@RequestBody RoomDto input) throws Exception {
        return _RoomService.CreateOrEdit(input);
    }
    /**
     * 自习室删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        _RoomService.Delete(input);
    }

    /**
     * 自习室批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        _RoomService.BatchDelete(input);
    }



}
