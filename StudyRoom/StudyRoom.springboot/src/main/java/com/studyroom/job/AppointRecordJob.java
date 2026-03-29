package com.studyroom.job;

import com.studyroom.service.AppointRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppointRecordJob {

    @Autowired
    private AppointRecordService _AppointRecordService;

    /**
     * 每隔10s检测座位预约是否结束
     */
    @Scheduled(cron = "*/10 * * * * ?")
    private void AutoCompletedAppoint() {
        long nowDateTime = System.currentTimeMillis();
        System.out.println("固定定时任务执行:--->" + nowDateTime + "，任务名称:每隔10s检测座位预约是否结束");
        _AppointRecordService.AutoCompletedAppoint();
    }
    /**
     * 每隔10s检测自动逾期
     */
    @Scheduled(cron = "*/10 * * * * ?")
    private void AutoOverdueTimes() {
        long nowDateTime = System.currentTimeMillis();
        System.out.println("固定定时任务执行:--->" + nowDateTime + "，任务名称:每隔10s检测自动逾期");
        _AppointRecordService.AutoOverdueTimes();
    }

}
