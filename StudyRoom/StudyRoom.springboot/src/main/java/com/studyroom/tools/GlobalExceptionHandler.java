package com.studyroom.tools;

import com.studyroom.SysConst;
import com.studyroom.tools.dto.ResponseData;
import com.studyroom.tools.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * 全局异常处理切面
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseData<String> bizExceptionHandler(CustomException e) {
        ResponseData<String> responseData = new ResponseData<>();
        responseData.setCode(SysConst.STATUS_500);
        responseData.setMsg(e.getErrorMsg());
        responseData.setSuccess(false);
        return responseData;
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData<String> validationExceptionHandler(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        ResponseData<String> responseData = new ResponseData<>();
        responseData.setCode(SysConst.STATUS_500);
        responseData.setMsg(msg);
        responseData.setSuccess(false);
        return responseData;
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData<String> exceptionHandler(Exception e) {
        String message = "系统异常,请联系管理员";
        if (e instanceof org.mybatis.spring.MyBatisSystemException) {
            org.mybatis.spring.MyBatisSystemException myBatisSystemException = (org.mybatis.spring.MyBatisSystemException) e;
            message = myBatisSystemException.getCause().getMessage();
            if (message.contains("Failed to obtain JDBC")) {
                message = "数据库没有链接上,1.请检查数据库的账号和密码是否正确,2.请检查数据库是否启动,3.请检查数据库是否正常运行，4.请检查数据库配置正确";
            }
        }
        log.error("系统异常: {}", e.getMessage(), e);
        ResponseData<String> responseData = new ResponseData<>();
        responseData.setCode(SysConst.STATUS_500);
        responseData.setMsg(message);
        responseData.setSuccess(false);
        return responseData;
    }
}
