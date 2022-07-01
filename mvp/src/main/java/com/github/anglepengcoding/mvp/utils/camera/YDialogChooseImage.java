package com.github.anglepengcoding.mvp.utils.camera;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.github.anglepengcoding.mvp.R;
import com.luck.picture.lib.utils.ToastUtils;

import androidx.fragment.app.Fragment;

/**
 * Created by 刘红鹏 on 2022/2/21.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 * 调用相机
 */
public class YDialogChooseImage extends YDialog {

    private LayoutType mLayoutType = LayoutType.TITLE;
    private TextView mTvCamera;
    private TextView mTvFile;
    private TextView mTvCancel;

    public YDialogChooseImage(Activity context) {
        super(context);
        initView(context);
    }

    public YDialogChooseImage(Fragment fragment) {
        super(fragment.getContext());
        initView(fragment);
    }

    public YDialogChooseImage(Activity context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public YDialogChooseImage(Fragment fragment, int themeResId) {
        super(fragment.getContext(), themeResId);
        initView(fragment);
    }

    public YDialogChooseImage(Activity context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    public YDialogChooseImage(Fragment fragment, float alpha, int gravity) {
        super(fragment.getContext(), alpha, gravity);
        initView(fragment);
    }

    public YDialogChooseImage(Fragment fragment, LayoutType layoutType) {
        super(fragment.getContext());
        mLayoutType = layoutType;
        initView(fragment);
    }


    public YDialogChooseImage(Activity context, LayoutType layoutType) {
        super(context);
        mLayoutType = layoutType;
        initView(context);
    }

    public YDialogChooseImage(Activity context, int themeResId, LayoutType layoutType) {
        super(context, themeResId);
        mLayoutType = layoutType;
        initView(context);
    }

    public YDialogChooseImage(Fragment fragment, int themeResId, LayoutType layoutType) {
        super(fragment.getContext(), themeResId);
        mLayoutType = layoutType;
        initView(fragment);
    }

    public YDialogChooseImage(Activity context, float alpha, int gravity, LayoutType layoutType) {
        super(context, alpha, gravity);
        mLayoutType = layoutType;
        initView(context);
    }

    public YDialogChooseImage(Fragment fragment, float alpha, int gravity, LayoutType layoutType) {
        super(fragment.getContext(), alpha, gravity);
        mLayoutType = layoutType;
        initView(fragment);
    }

    public TextView getTvCamera() {
        return mTvCamera;
    }

    public TextView getTvFile() {
        return mTvFile;
    }

    public TextView getTvCancel() {
        return mTvCancel;
    }

    public LayoutType getLayoutType() {
        return mLayoutType;
    }

    private void initView(final Activity activity) {
        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }


        mTvCamera = dialog_view.findViewById(R.id.tv_camera);
        mTvFile = dialog_view.findViewById(R.id.tv_file);
        mTvCancel = dialog_view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(arg0 -> cancel());
        mTvCamera.setOnClickListener(arg0 -> {
            //请求Camera权限
            YPermissionsUtils.requestCamera(activity, new onRequestPermissionsListener() {
                @Override
                public void onRequestBefore() {
                    ToastUtils.showToast(activity, "请先获取相机权限");
                }

                @Override
                public void onRequestLater() {
                    YPhotoUtils.openCameraImage(activity);
                    cancel();
                }
            });
        });
        mTvFile.setOnClickListener(arg0 -> YPermissionsUtils.requestReadExternalStorage(mContext, new onRequestPermissionsListener() {
            @Override
            public void onRequestBefore() {
                cancel();
                ToastUtils.showToast(activity, "请先获取读取SDCard权限");
                return;
            }

            @Override
            public void onRequestLater() {
                YPhotoUtils.openLocalImage(activity);
                cancel();
            }
        }));
        setContentView(dialog_view);
        mLayoutParams.gravity = Gravity.BOTTOM;
    }

    private void initView(final Fragment fragment) {
        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }

        mTvCamera = (TextView) dialog_view.findViewById(R.id.tv_camera);
        mTvFile = (TextView) dialog_view.findViewById(R.id.tv_file);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(arg0 -> cancel());
        mTvCamera.setOnClickListener(arg0 -> {
            //请求Camera权限
            YPermissionsUtils.requestCamera(fragment.getContext(), new onRequestPermissionsListener() {
                @Override
                public void onRequestBefore() {
                    ToastUtils.showToast(getContext(), "请先获取相机权限");
                }

                @Override
                public void onRequestLater() {
                    YPhotoUtils.openCameraImage(fragment);
                    cancel();
                }
            });
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                YPermissionsUtils.requestReadExternalStorage(mContext, new onRequestPermissionsListener() {
                    @Override
                    public void onRequestBefore() {
                        cancel();
                        ToastUtils.showToast(fragment.getContext(), "请先获取读取SDCard权限");
                        return;
                    }

                    @Override
                    public void onRequestLater() {
                        YPhotoUtils.openLocalImage(fragment);
                        cancel();
                    }
                });
            }
        });

        setContentView(dialog_view);
        mLayoutParams.gravity = Gravity.BOTTOM;
    }

    public enum LayoutType {
        TITLE, NO_TITLE
    }

}