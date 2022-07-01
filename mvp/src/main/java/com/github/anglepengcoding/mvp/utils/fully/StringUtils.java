package com.github.anglepengcoding.mvp.utils.fully;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;



/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.tool
 * email：893855882@qq.com
 * data：2017/5/25
 */

public class StringUtils {
    public static boolean isCamera(String title) {
        if (!TextUtils.isEmpty(title) && title.startsWith("相机胶卷")
                || title.startsWith("CameraRoll")
                || title.startsWith("所有音频")
                || title.startsWith("All audio")) {
            return true;
        }

        return false;
    }


    public static void modifyTextViewDrawable(TextView v, Drawable drawable, int index) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //index 0:左 1：上 2：右 3：下
        if (index == 0) {
            v.setCompoundDrawables(drawable, null, null, null);
        } else if (index == 1) {
            v.setCompoundDrawables(null, drawable, null, null);
        } else if (index == 2) {
            v.setCompoundDrawables(null, null, drawable, null);
        } else {
            v.setCompoundDrawables(null, null, null, drawable);
        }
    }

}
