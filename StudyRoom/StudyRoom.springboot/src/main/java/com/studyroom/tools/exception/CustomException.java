package com.studyroom.tools.exception;

/**
 * 自定义全局异常
 */
public class CustomException extends  RuntimeException{

    protected Integer errorCode;
    protected String errorMsg;

    public CustomException(){

    }
    public CustomException(String errorMsg) {

        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
