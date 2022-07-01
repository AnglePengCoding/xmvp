package com.github.anglepengcoding.mvp.net;

/**
 * Created by
 *
 * @author Angle
 *         <p>https://github.com/AnglePengCoding</p>
 *         <p>https://blog.csdn.net/LIU_HONGPENG</p>
 *         <p>on 2021/3/10</p>
 */

public class ErrorResponse {
    private String code;
    private String data;
    private String msg;

    public ErrorResponse(String code, String data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
