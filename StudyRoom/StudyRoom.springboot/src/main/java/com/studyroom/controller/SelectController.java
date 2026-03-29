package com.studyroom.controller;

import com.studyroom.enums.*;
import com.studyroom.tools.dto.PagedResult;
import com.studyroom.tools.dto.SelectResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/Select")
public class SelectController {

    @RequestMapping(value = "/RoleType", method = RequestMethod.POST)
    public PagedResult<SelectResult> RoleType() {

        List<SelectResult> rs = Arrays.stream(RoleTypeEnum.values()).map(n -> new SelectResult(n.toString(), n.name(), n.index() + "", "")).toList();
        return PagedResult.GetInstance(rs, rs.stream().count());
    }

    /**
     * 预约状态枚举接口
     */
    @RequestMapping(value = "/AppointStatusEnum", method = RequestMethod.POST)
    public PagedResult<SelectResult> AppointStatusEnum() {

        var rs = Arrays.stream(AppointStatusEnum.values()).map(n -> new SelectResult(n.toString(), n.name(), Integer.toString(n.index()), "")).toList();
        return PagedResult.GetInstance(rs, rs.stream().count());
    }

    /**
     * 预约时间段枚举接口
     */
    @RequestMapping(value = "/AppointDateTypeEnum", method = RequestMethod.POST)
    public PagedResult<SelectResult> AppointDateTypeEnum() {

        var rs = Arrays.stream(AppointDateTypeEnum.values()).map(n -> new SelectResult(n.toString(), n.name(), Integer.toString(n.index()), "")).toList();
        return PagedResult.GetInstance(rs, rs.stream().count());
    }

}
