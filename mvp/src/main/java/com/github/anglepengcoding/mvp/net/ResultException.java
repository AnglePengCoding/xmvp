package com.github.anglepengcoding.mvp.net;

import java.io.IOException;

/**
 * Created by
 *
 * @author Angle
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 * <p>on 2021/3/10</p>
 */

public class ResultException extends IOException {
    private String code;
    private String msg;

    public ResultException(String code, String msg) {
        this.code = code;
        this.msg = msg;
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

}
