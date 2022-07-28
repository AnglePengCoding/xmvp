package com.github.anglepengcoding.xmvp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.anglepengcoding.mvp.base.BaseActivity;
import com.github.anglepengcoding.mvp.base.BaseFragment;
import com.github.anglepengcoding.mvp.utils.camera.YPermissionsUtils;
import com.github.anglepengcoding.mvp.utils.camera.onRequestPermissionsListener;
import com.github.anglepengcoding.mvp.utils.dialog.ProgressDialogUtils;
import com.github.anglepengcoding.xmvp.databinding.FragmentSoilBinding;
import com.tbruyelle.rxpermissions3.Permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


public class SoilFragment extends BaseFragment<FragmentSoilBinding, SoilPresenter, SoilModel> implements SoilContract.View {

    public static SoilFragment newInstance() {
        return new SoilFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        binding.mBt.setOnClickListener(v -> {
            mPresenter.sweepCode("111");
        });
    }
}
