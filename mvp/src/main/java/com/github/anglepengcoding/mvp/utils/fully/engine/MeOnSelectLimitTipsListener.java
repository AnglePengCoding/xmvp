package com.github.anglepengcoding.mvp.utils.fully.engine;

import android.content.Context;

import com.github.anglepengcoding.mvp.R;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectLimitType;
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.lib.utils.ToastUtils;

/**
 * Created by 刘红鹏 on 2022/3/1.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class MeOnSelectLimitTipsListener implements OnSelectLimitTipsListener {

    @Override
    public boolean onSelectLimitTips(Context context, PictureSelectionConfig config, int limitType) {
        if (limitType == SelectLimitType.SELECT_MAX_VIDEO_SELECT_LIMIT) {
            ToastUtils.showToast(context, context.getString(R.string.ps_message_video_max_num, String.valueOf(config.maxVideoSelectNum)));
            return true;
        }
        return false;
    }
}
