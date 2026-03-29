package com.studyroom.tools;

import com.studyroom.SysConst;
import com.studyroom.tools.dto.ResponseData;
import com.studyroom.tools.exception.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 全局异常处理切面
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseData bizExceptionHandler(CustomException e) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(SysConst.STATUS_500);
        responseData.setMsg(e.getErrorMsg());
        responseData.setSuccess(false);
        return responseData;
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData exceptionHandler(Exception e) {
        String message = "系统异常,请联系管理员";
        if (e instanceof org.mybatis.spring.MyBatisSystemException) {
            // 转换成具体的类型
            org.mybatis.spring.MyBatisSystemException myBatisSystemException = (org.mybatis.spring.MyBatisSystemException) e;
            message = myBatisSystemException.getCause().getMessage();
            if (message.contains("Failed to obtain JDBC")) {
                message = "数据库没有链接上,1.请检查数据库的账号和密码是否正确,2.请检查数据库是否启动,3.请检查数据库是否正常运行，4.请检查数据库配置正确";
            }

        }

        ResponseData responseData = new ResponseData();
        responseData.setCode(SysConst.STATUS_500);
        e.printStackTrace();

        System.out.println(e.getMessage());
        responseData.setMsg(message);
        responseData.setSuccess(false);
        return responseData;
    }
}
