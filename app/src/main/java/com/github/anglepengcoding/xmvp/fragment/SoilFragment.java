package com.github.anglepengcoding.xmvp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.anglepengcoding.mvp.base.BaseFragment;
import com.github.anglepengcoding.xmvp.databinding.FragmentSoilBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SoilFragment extends BaseFragment<FragmentSoilBinding, SoilPresenter, SoilModel> implements SoilContract.View {

    public static SoilFragment newInstance( ){
        return new SoilFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mTv.setText("Yuang");
    }

    @Override
    public void initView() {
    }

    @Override
    public void initPresenter() {

    }

}
