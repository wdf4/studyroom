package com.studyroom.tools.dto;

import com.studyroom.SysConst;
import lombok.Data;

@Data
public class ResponseData<T> {
    public String Msg;

    public T Data;

    public String Code;

    public Boolean Success;

    /**
     *得到一个响应的实例
     * @return
     */
    public static <T> ResponseData<T> GetResponseDataInstance(T data,String msg,Boolean success) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setSuccess(success);
        responseData.setData(data);
        responseData.setMsg(msg);
        responseData.setCode(SysConst.STATUS_200);
        return responseData;
    }
    /**
     * 响应一个没有消息体成功
     * @return
     */
    public static <T> ResponseData<T> OfSuccess() {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setSuccess(true);
        responseData.setMsg("成功");
        responseData.setCode(SysConst.STATUS_200);
        return responseData;
    }


}
