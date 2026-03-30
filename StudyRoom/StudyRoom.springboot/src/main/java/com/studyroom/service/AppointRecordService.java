package com.studyroom.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studyroom.dto.*;
import com.studyroom.dto.query.*;
import com.studyroom.entity.*;
import com.studyroom.tools.dto.*;
import java.util.HashMap;
import java.util.List;
/**
 * 预约记录功能的Service接口的定义清单
 */
public interface AppointRecordService extends IService<AppointRecord> {

    /**
     * 预约记录的分页查询方法接口定义
     */
    public PagedResult<AppointRecordDto> List(AppointRecordPagedInput input) ;
    /**
     * 预约记录的新增或者修改方法接口定义
     */
    public AppointRecordDto CreateOrEdit(AppointRecordDto input);

     /**
     * 获取预约记录信息
     */
    public AppointRecordDto Get(AppointRecordPagedInput input);
 	 /**
     * 预约记录删除
     */
    public void Delete(IdInput input);

    /**
     * 预约记录批量删除
     */
    public void BatchDelete(IdsInput input);

    /**
     * 预约之前检测是否满足预约条件
     */
    void CheckIsAbleAppoint(AppointRecordDto input);
    /**
     * 选座确认
     */
    AppointRecordDto ToOrder(AppointRecordDto input);
    /**
     * 到场打卡
     */
    void ArrivalClock(AppointRecordDto input);
    /**
     * 取消预约
     */
    void CancelAppoint(AppointRecordDto input);
    /**
     * 提前结束
     */
    void EarlyEnd(AppointRecordDto input);
    /**
     * 评论评分
     */
    void Comment(AppointRecordDto input);
    /**
     * 自动完成
     */
    void AutoCompletedAppoint();
    /**
     * 自动逾期
     */
    void AutoOverdueTimes();
    /**
     * 统计早中晚每个自习室的使用率&空闲率
     */
    List<Object> GetAppointRoomUseRate(AppointRoomUseRateQueryInput input);

    /**
     * 统计各类数据到看板
     */
    HashMap<String, Object> GetDataCollect();
    /**
     * 统计各个自习室预约的状态信息
     */
    List<Object> GetAppointRoomAppointStatusData(AppointRoomAppointStatusDataQueryInput input);


    /**
     * 统计每个自习室实时在场的人数
     */
    List<Object> GetAppointRoomRealTimeData();
}
