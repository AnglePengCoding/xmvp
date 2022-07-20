package com.github.anglepengcoding.mvp.utils.dialog;

import android.content.Context;
import android.graphics.Color;

import com.github.anglepengcoding.mvp.widget.dialog.PictureDialog;
import com.yk.loading.LoadingDialog;


/**
 * Created by 刘红鹏 on 2022/2/18.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class ProgressDialogUtils {

    private static ProgressDialogUtils instance;
    private LoadingDialog dialog;

    public static synchronized ProgressDialogUtils $(Context context) {
        if (null == instance) {
            instance = new ProgressDialogUtils(context);
        }
        return instance;
    }


    public ProgressDialogUtils(Context context) {
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(context)
                .setMessage("加载中...")//设置提示文字
                .setCancelable(true)//按返回键取消
                .setMessageColor(Color.WHITE)//提示文字颜色
                .setMessageSize(14)//提示文字字号
                .setBackgroundTransparent(true)//弹窗背景色是透明或半透明
                .setCancelOutside(true);//点击空白区域弹消失
        dialog = loadBuilder.create();
    }

    public ProgressDialogUtils(Context context, String message, boolean cancelable, int color,
                               int messageSize, boolean isBackgroundTransparent, boolean isCancelOutside) {
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(context)
                .setMessage(message)//设置提示文字
                .setCancelable(cancelable)//按返回键取消
                .setMessageColor(color)//提示文字颜色
                .setMessageSize(messageSize)//提示文字字号
                .setBackgroundTransparent(isBackgroundTransparent)//弹窗背景色是透明或半透明
                .setCancelOutside(isCancelOutside);//点击空白区域弹消失
        dialog = loadBuilder.create();
    }

    public void showProgress() {
        dialog.show();
    }

    public void dismissProgress() {
        dialog.dismiss();
    }
}
