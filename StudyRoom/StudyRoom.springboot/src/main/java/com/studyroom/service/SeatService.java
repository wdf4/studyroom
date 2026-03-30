package com.studyroom.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studyroom.dto.*;
import com.studyroom.dto.query.*;
import com.studyroom.entity.*;
import com.studyroom.tools.dto.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
/**
 * 座位功能的Service接口的定义清单
 */
public interface SeatService extends IService<Seat> {

    /**
     * 座位的分页查询方法接口定义
     */
    public PagedResult<SeatDto> List(SeatPagedInput input) ;
    /**
     * 座位的新增或者修改方法接口定义
     */
    public SeatDto CreateOrEdit(SeatDto input);

     /**
     * 获取座位信息
     */
    public SeatDto Get(SeatPagedInput input);

    /**
     * 批量创建座位
     */
    void BatchCreate(List<SeatDto> inputs);

    /**
     * 座位删除
     */
    public void Delete(IdInput input);

    /**
     * 座位批量删除
     */
    public void BatchDelete(IdsInput input);


    /**
     * 得到今天到未来7天的日期
     */
    List<LocalDate> GetSevenDays();
    /**
     * 得到选中日期的座位信息
     */
    SeatArrange GetArrange(SeatPagedInput input) throws InvocationTargetException, IllegalAccessException;
}
