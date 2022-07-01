package com.github.anglepengcoding.xmvp;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.github.anglepengcoding.mvp.base.BaseActivity;
import com.github.anglepengcoding.mvp.sharedpreferences.LocalDataManager;
import com.github.anglepengcoding.xmvp.databinding.ActivityMainBinding;

import androidx.annotation.Nullable;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter, MainModel> implements MainContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void save(View view) {
        UserBean userBean = new UserBean();
        userBean.setType("123");
        LocalDataManager.getInstance().saveLoginInfo(userBean);
    }

    public void read(View view) {
        binding.mTvRead.setText(LocalDataManager.getInstance()
                .getLoginInfo(UserBean.class).getType());
    }
}