package com.github.anglepengcoding.mvp.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.github.anglepengcoding.mvp.sharedpreferences.PrefsHelper;
import com.github.anglepengcoding.mvp.utils.Utils;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import retrofit2.Converter;


/**
 * Created by 刘红鹏 on 2022/2/16.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public abstract class BaseApp extends Application {
    private static BaseApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Utils.init(this);
        PrefsHelper.init(this);

        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setBaseOnWidth(true)
                .setCustomFragment(true);
    }

    public static synchronized BaseApp getInstance() {
        return mApp;
    }

    public static Context getAppContext() {
        return mApp.getApplicationContext();
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }


    /**
     * 服务器地址
     *
     * @return 请求地址
     */
    public abstract String setBaseUrl();

    /**
     * 服务器返回成功code，一般成功返回code默认200
     *
     * @return
     */
    public abstract String setResultCodeOperationSuccessful();
}
