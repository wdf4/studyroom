package com.studyroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyroom.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表对应的Mapper
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

}
