package com.studyroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyroom.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约记录表对应的Mapper
 */
@Mapper
public interface AppointRecordMapper extends BaseMapper<AppointRecord> {

}
