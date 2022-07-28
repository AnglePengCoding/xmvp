package com.github.anglepengcoding.mvp.utils.picture_selector;

import android.content.Context;

import com.github.anglepengcoding.mvp.utils.fully.engine.ImageCompressEngine;
import com.github.anglepengcoding.mvp.utils.fully.engine.ImageLoadEngine;
import com.github.anglepengcoding.mvp.utils.fully.engine.MeOnSelectLimitTipsListener;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.style.PictureSelectorStyle;

import java.util.List;


public class PictureSelectorUtils {

    public static void selector(Context context, List<LocalMedia> selectedList, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(context)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(new PictureSelectorStyle())
                .setImageEngine(new ImageLoadEngine())
                .setCompressEngine(new ImageCompressEngine())
                .setSelectLimitTipsListener(new MeOnSelectLimitTipsListener())
                .setLanguage(LanguageConfig.CHINESE)
                .isDisplayCamera(false)
                .isOpenClickSound(false)
                .isFastSlidingSelect(false)
                .isWithSelectVideoImage(true)
                .isPreviewFullScreenMode(true)
                .isPreviewZoomEffect(true)
                .isPreviewImage(true)
                .isPreviewVideo(true)
                .isPreviewAudio(true)
                .isMaxSelectEnabledMask(true)
                .isDirectReturnSingle(false)
                .setMaxSelectNum(3)
                .isGif(false)
                .setSelectedData(selectedList)
                .forResult(call);

    }

}
