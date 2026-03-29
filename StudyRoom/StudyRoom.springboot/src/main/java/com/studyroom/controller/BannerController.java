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
 * 封面控制器
 */
@RestController()
@RequestMapping("/Banner")
public class BannerController {
    @Autowired()
    private  BannerService _BannerService;
    @Autowired()
    private BannerMapper _BannerMapper;
    /**
     * 封面分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<BannerDto> List(@RequestBody BannerPagedInput input)  {
        return _BannerService.List(input);
    }
     /**
     * 单个封面查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public BannerDto Get(@RequestBody BannerPagedInput input) {

        return _BannerService.Get(input);
    }

    /**
     * 封面创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public BannerDto CreateOrEdit(@RequestBody BannerDto input) throws Exception {
        return _BannerService.CreateOrEdit(input);
    }
    /**
     * 封面删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        _BannerService.Delete(input);
    }

    /**
     * 封面批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        _BannerService.BatchDelete(input);
    }



}
