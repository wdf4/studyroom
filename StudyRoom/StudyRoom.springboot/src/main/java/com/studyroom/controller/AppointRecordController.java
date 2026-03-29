package com.studyroom.controller;

import com.studyroom.SysConst;
import com.studyroom.dto.*;
import com.studyroom.dto.query.*;
import com.studyroom.entity.*;
import com.studyroom.mapper.*;
import com.studyroom.service.*;
import com.studyroom.tools.dto.*;
import com.studyroom.tools.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 预约记录控制器
 */
@RestController()
@RequestMapping("/AppointRecord")
public class AppointRecordController {
    @Autowired()
    private AppointRecordService _AppointRecordService;
    @Autowired()
    private AppointRecordMapper _AppointRecordMapper;

    /**
     * 预约记录分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<AppointRecordDto> List(@RequestBody AppointRecordPagedInput input) {
        return _AppointRecordService.List(input);
    }

    /**
     * 单个预约记录查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public AppointRecordDto Get(@RequestBody AppointRecordPagedInput input) {

        return _AppointRecordService.Get(input);
    }

    /**
     * 预约记录创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public AppointRecordDto CreateOrEdit(@RequestBody AppointRecordDto input) throws Exception {
        return _AppointRecordService.CreateOrEdit(input);
    }

    /**
     * 预约记录删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input) {
        _AppointRecordService.Delete(input);
    }

    /**
     * 预约记录批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input) {
        _AppointRecordService.BatchDelete(input);
    }

    /**
     * 预约之前检测是否满足预约条件
     */
    @RequestMapping(value = "/CheckIsAbleAppoint", method = RequestMethod.POST)
    public void CheckIsAbleAppoint(@RequestBody AppointRecordDto input) {
        _AppointRecordService.CheckIsAbleAppoint(input);
    }

    /**
     * 选座确认
     */
    @RequestMapping(value = "/ToOrder", method = RequestMethod.POST)
    public AppointRecordDto ToOrder(@RequestBody AppointRecordDto input) {
        return _AppointRecordService.ToOrder(input);
    }

    /**
     * 到场打卡
     */
    @RequestMapping(value = "/ArrivalClock", method = RequestMethod.POST)
    public void ArrivalClock(@RequestBody AppointRecordDto input) {
        _AppointRecordService.ArrivalClock(input);
    }

    /**
     * 取消预约
     */
    @RequestMapping(value = "/CancelAppoint", method = RequestMethod.POST)
    public void CancelAppoint(@RequestBody AppointRecordDto input) {
        _AppointRecordService.CancelAppoint(input);
    }

    /**
     * 提前结束
     */
    @RequestMapping(value = "/EarlyEnd", method = RequestMethod.POST)
    public void EarlyEnd(@RequestBody AppointRecordDto input) {
        _AppointRecordService.EarlyEnd(input);
    }

    /**
     * 评论评分
     */
    @RequestMapping(value = "/Comment", method = RequestMethod.POST)
    public void Comment(@RequestBody AppointRecordDto input) {
        _AppointRecordService.Comment(input);
    }

    /**
     * 统计早中晚每个自习室的使用率&空闲率
     */
    @RequestMapping(value = "/GetAppointRoomUseRate", method = RequestMethod.POST)
    public List<Object> GetAppointRoomUseRate(@RequestBody AppointRoomUseRateQueryInput input) {
        return _AppointRecordService.GetAppointRoomUseRate(input);
    }

    /**
     * 统计各类数据到看板
     */
    @RequestMapping(value = "/GetDataCollect", method = RequestMethod.POST)
    public HashMap<String, Object> GetDataCollect() {
        return _AppointRecordService.GetDataCollect();
    }

    /**
     * 统计各个自习室预约的状态信息
     */
    @RequestMapping(value = "/GetAppointRoomAppointStatusData", method = RequestMethod.POST)
    public List<Object> GetAppointRoomAppointStatusData(@RequestBody AppointRoomAppointStatusDataQueryInput input) {
        return _AppointRecordService.GetAppointRoomAppointStatusData(input);
    }

    /**
     * 统计每个自习室实时在场的人数
     */
    @RequestMapping(value = "/GetAppointRoomRealTimeData", method = RequestMethod.POST)
    public List<Object> GetAppointRoomRealTimeData() {
        return _AppointRecordService.GetAppointRoomRealTimeData();
    }
}
