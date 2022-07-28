package com.github.anglepengcoding.mvp.utils.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by 刘红鹏 on 2022/2/21.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class YPermissionsUtils {

    //请求Camera权限
    public static void requestCamera(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    //请求位置权限
    public static void requestLocation(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    //打电话权限
    public static void requestCall(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    //写入权限
    public static void requestWriteExternalStorage(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }
    //读取权限
    public static void requestReadExternalStorage(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }

    /**
     * 录制视频需要的权限
     * @param mContext
     * @param onRequestPermissionsListener
     */
    public static void requestRecordVideo(Context mContext, onRequestPermissionsListener onRequestPermissionsListener) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO}, 1);
            onRequestPermissionsListener.onRequestBefore();
        } else {
            onRequestPermissionsListener.onRequestLater();
        }
    }
}
