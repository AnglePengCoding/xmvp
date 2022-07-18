package com.github.anglepengcoding.mvp.base;


import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Created by 刘红鹏 on 2022/2/15.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public interface BaseUiInterface {
    String ERROR_CODE = "NullPointerException";

    Context getContext();
    @NonNull
    Activity getAActivity();
    /**
     * 无网络连接
     */
    void showNoNetworkConnection();

    /**
     * 抛出未知错误
     */
    void showUnknownException();

    /**
     * 数据解析出错/服务器错误异常
     */
    void showDataException(String msg);


}
