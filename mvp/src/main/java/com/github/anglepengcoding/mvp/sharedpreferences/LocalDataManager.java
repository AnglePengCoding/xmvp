package com.github.anglepengcoding.mvp.sharedpreferences;


import androidx.annotation.NonNull;

/**
 * Created by 刘红鹏 on 2022/2/22.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class LocalDataManager {

    private static LocalDataManager instance;

    private LocalDataManager() {

    }

    public static LocalDataManager getInstance() {
        if (instance == null) {
            synchronized (LocalDataManager.class) {
                if (instance == null) {
                    instance = new LocalDataManager();
                }
            }
        }
        return instance;
    }

    public <T> void saveLoginInfo(@NonNull T classOfT) {
        //Update cached object!
        PrefsHelper.setLoginInfo(classOfT);
    }

    public void clearLoginInfo() {
        PrefsHelper.removeLoginInfo();
    }

    public <T> T getLoginInfo(Class<T> classOfT) {
        return PrefsHelper.getLoginInfo(classOfT);
    }

}
