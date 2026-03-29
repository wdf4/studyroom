package com.studyroom.tools;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.studyroom.tools.dto.CurrentUserDto;
import org.apache.ibatis.reflection.MetaObject;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * mybatis 插入数据之前进行的元数据处理
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    /**
     * 当前用户信息
     */
    private CurrentUserDto  currentUserDto;
    public MyMetaObjectHandler( ) {

    }

    /**
     * 数据库执行insert时候的 处理
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {


        //创建的时候给表创建时间赋值
        if(this.getFieldValByName("CreationTime",metaObject)==null) {
            this.setFieldValByName("CreationTime", LocalDateTime.now(), metaObject);
        }
        //获取当前请求管道中的用户信息
        currentUserDto=BaseContext.getCurrentUserDto();
        //如果当前上下文有用户 则给创建人进行赋值
        if(currentUserDto!=null)
        {
            //如果这个表存在CreatorId这个字段 并且为null 我们对他进行赋值
            if(this.getFieldValByName("CreatorId",metaObject)==null) {
                this.setFieldValByName("CreatorId", currentUserDto.getUserId(), metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
