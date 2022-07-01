package com.github.anglepengcoding.mvp.sharedpreferences;

import android.content.Context;
import android.text.TextUtils;

import com.github.anglepengcoding.mvp.utils.Logg;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 刘红鹏 on 2022/2/22.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class PrefsHelper {


    private static final String PREFERENCE_FILE_NAME = "BeautyLivePrefs";

    private static final String KEY_IS_FIRST_RUN = "isFirstRun";

    private static final String KEY_LOGIN_INFO = "loginInfo";


    public static void init(Context context) {
        Prefs.init(context, PREFERENCE_FILE_NAME);
    }

    public static boolean getIsFirstRun() {
        //首次启动这个方法比较特别，默认返回true，而不是一般Boolean的false
        return Prefs.get(KEY_IS_FIRST_RUN, true);
    }

    public static void setIsFirstRun(boolean isFirstRun) {
        Prefs.set(KEY_IS_FIRST_RUN, isFirstRun);
    }

    /**
     * 保存用户的登录数据。
     */
    public static <T> void setLoginInfo(@NonNull T classOfT) {
        String infoToSave = new Gson().toJson(classOfT);
        Logg.d("LoginInfoToSp: " + infoToSave);
        Prefs.set(KEY_LOGIN_INFO, infoToSave);
    }

    /**
     * 清除登录数据。
     */
    public static void removeLoginInfo() {
        Logg.d("Removing login info.");
        Prefs.remove(KEY_LOGIN_INFO);
    }

    /**
     * 查询存储的用户登录数据，如果不存在则返回null。
     */
    @Nullable
    public static <T> T getLoginInfo(Class<T> classOfT) {
        String savedInfo = Prefs.getString(KEY_LOGIN_INFO);
        Logg.i("LoginInfoFromSp:" + savedInfo);
        if (TextUtils.isEmpty(savedInfo)) {
            return null;
        }
        return new Gson().fromJson(savedInfo, classOfT);
    }

}
