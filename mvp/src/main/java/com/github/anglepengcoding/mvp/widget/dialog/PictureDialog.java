package com.github.anglepengcoding.mvp.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.github.anglepengcoding.mvp.R;


/**
 * Created by 刘红鹏 on 2022/2/18.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 * 网络加载
 */
public class PictureDialog extends Dialog {

    public PictureDialog(Context context) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.loading_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);

        // 解决dialog背景四个直角有黑边问题
        window.setBackgroundDrawableResource(android.R.color.transparent);// 背景透明
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏（注：内容区域全屏，有状态显示）
    }


}
