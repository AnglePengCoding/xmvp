package com.github.anglepengcoding.mvp.utils.fully.engine;



import com.github.anglepengcoding.mvp.base.BaseApp;

import java.io.File;

/**
 * Created by 刘红鹏 on 2022/3/1.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public final class CameraOutputUtils {

    /**
     * 创建相机自定义输出目录
     *
     * @return
     */
    public static String getSandboxCameraOutputPath() {
            File externalFilesDir = BaseApp.getAppContext().getExternalFilesDir("");
            File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
            if (!customFile.exists()) {
                customFile.mkdirs();
            }
            return customFile.getAbsolutePath() + File.separator;
    }
    /**
     * 创建音频自定义输出目录
     *
     * @return
     */
    public static String getSandboxAudioOutputPath() {
            File externalFilesDir = BaseApp.getAppContext().getExternalFilesDir("");
            File customFile = new File(externalFilesDir.getAbsolutePath(), "Sound");
            if (!customFile.exists()) {
                customFile.mkdirs();
            }
            return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getSandboxPath() {
        File externalFilesDir = BaseApp.getAppContext().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }
}
