package com.github.anglepengcoding.mvp.utils.dialog;

import android.content.Context;

import com.github.anglepengcoding.mvp.widget.dialog.PictureDialog;


/**
 * Created by 刘红鹏 on 2022/2/18.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class ProgressDialogUtils {

    private static ProgressDialogUtils instance;
    private PictureDialog dialog;

    public static synchronized ProgressDialogUtils $() {
        if (null == instance) {
            instance = new ProgressDialogUtils();
        }
        return instance;
    }

    public void showProgress(Context context) {
        dialog = new PictureDialog(context);
        dialog.show();
    }

    public void dismissProgress( ) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
