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
 * 积分控制器
 */
@RestController()
@RequestMapping("/Integral")
public class IntegralController {
    @Autowired()
    private IntegralService _IntegralService;
    @Autowired()
    private IntegralMapper _IntegralMapper;

    /**
     * 积分分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<IntegralDto> List(@RequestBody IntegralPagedInput input) {
        return _IntegralService.List(input);
    }

    /**
     * 单个积分查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public IntegralDto Get(@RequestBody IntegralPagedInput input) {

        return _IntegralService.Get(input);
    }

    /**
     * 积分创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public IntegralDto CreateOrEdit(@RequestBody IntegralDto input) throws Exception {
        return _IntegralService.CreateOrEdit(input);
    }

    /**
     * 积分删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input) {
        _IntegralService.Delete(input);
    }

    /**
     * 积分批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input) {
        _IntegralService.BatchDelete(input);
    }


    /**
     * 得到我的积分数据
     */
    @RequestMapping(value = "/GetMyIntegralData", method = RequestMethod.POST)
    @SneakyThrows
    public MyIntegralDataDto GetMyIntegralData() {
        return _IntegralService.GetMyIntegralData();
    }

    /**
     * 清空逾期次数
     */
    @RequestMapping(value = "/OverdueTimesClear", method = RequestMethod.POST)
    @SneakyThrows
    public void OverdueTimesClear() {
        _IntegralService.OverdueTimesClear();
    }

    /**
     * 统计最近30天积分的消耗量和获取量折线图
     */
    @RequestMapping(value = "/GetIntegralConsumeAndGainChart", method = RequestMethod.POST)
    @SneakyThrows
    public List<Object> GetIntegralConsumeAndGainChart() {
        return _IntegralService.GetIntegralConsumeAndGainChart();
    }


}
