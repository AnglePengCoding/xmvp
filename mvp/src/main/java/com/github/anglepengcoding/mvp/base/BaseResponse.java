package com.github.anglepengcoding.mvp.base;


/**
 * Created by 刘红鹏 on 2022/2/17.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class BaseResponse<DataType > {

    public static final String RESULT_CODE_OPERATION_SUCCESSFUL = "200";//操作成功
    public static final String RESULT_CODE_OPERATION_FAILED = "2000";//操作失败
    public static final String RESULT_CODE_PARAMETER_EXCEPTION = "2001";//参数异常
    public static final String RESULT_CODE_SERVER_BUSY = "2003";//服务器正忙，请稍后重试
    public static final String RESULT_CODE_LOGIN_TIME_OUT = "3001";//登录超时,请重新登录
    public static final String RESULT_CODE_NOT_LOGGED = "3002";//您还未登录，请先登录
    public static final String RESULT_CODE_LOGIN_EXPIRED = "301";//登录过期，请重新登录
    public static final String RESULT_CODE_ILLEGAL_REQUEST = "4001";//非法请求
    public static final String RESULT_CODE_NOT_PERMISSION = "4002";//您还没该权限
    /**
     * 通用返回值属性
     */
    private String code;
    /**
     * 通用返回信息。
     */
    private String msg;
    /**
     * 具体的内容。
     */
    private DataType result;

    private Object ext;

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataType getData() {
        return result;
    }

    public void setData(DataType data) {
        this.result = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                ", ext=" + ext +
                '}';
    }
}
