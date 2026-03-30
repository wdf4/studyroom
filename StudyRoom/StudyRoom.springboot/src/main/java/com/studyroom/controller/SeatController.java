package com.studyroom.controller;

import com.studyroom.dto.SeatArrange;
import com.studyroom.dto.SeatDto;
import com.studyroom.dto.query.SeatPagedInput;

import com.studyroom.service.SeatService;
import com.studyroom.tools.dto.IdInput;
import com.studyroom.tools.dto.IdsInput;
import com.studyroom.tools.dto.PagedResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

/**
 * 座位控制器
 */
@RestController()
@RequestMapping("/Seat")
public class SeatController {
    @Autowired()
    private SeatService _SeatService;

    /**
     * 座位分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<SeatDto> List(@RequestBody SeatPagedInput input) {
        return _SeatService.List(input);
    }

    /**
     * 单个座位查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public SeatDto Get(@RequestBody SeatPagedInput input) {

        return _SeatService.Get(input);
    }

    /**
     * 座位创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public SeatDto CreateOrEdit(@RequestBody SeatDto input) throws Exception {
        return _SeatService.CreateOrEdit(input);
    }

    /**
     * 座位删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input) {
        _SeatService.Delete(input);
    }

    /**
     * 座位批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input) {
        _SeatService.BatchDelete(input);
    }

    /**
     * 批量创建座位
     */
    @RequestMapping(value = "/BatchCreate", method = RequestMethod.POST)
    public void BatchCreate(@RequestBody List<SeatDto> inputs) {
        _SeatService.BatchCreate(inputs);
    }

    /**
     * 得到今天到未来7天的日期
     */
    @RequestMapping(value = "/GetSevenDays", method = RequestMethod.POST)
    public List<LocalDate> GetSevenDays() {
        return _SeatService.GetSevenDays();
    }

    /**
     * 得到选中日期的座位信息
     */
    @RequestMapping(value = "/GetArrange", method = RequestMethod.POST)
    public SeatArrange GetArrange(@RequestBody SeatPagedInput input) throws InvocationTargetException, IllegalAccessException {
        return _SeatService.GetArrange(input);
    }

}
