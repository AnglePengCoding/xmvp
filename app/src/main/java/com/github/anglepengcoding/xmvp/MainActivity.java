package com.github.anglepengcoding.xmvp;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.github.anglepengcoding.mvp.base.BaseActivity;
import com.github.anglepengcoding.mvp.sharedpreferences.LocalDataManager;
import com.github.anglepengcoding.xmvp.bean.NavBean;
import com.github.anglepengcoding.xmvp.databinding.ActivityMainBinding;
import com.github.anglepengcoding.xmvp.fragment.FragmentAdapter;
import com.github.anglepengcoding.xmvp.fragment.SoilFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter, MainModel> implements MainContract.View {
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        initFragment();
    }

    private void initFragment() {
        ArrayList<NavBean> datas = new ArrayList<>();
        ArrayList mTitles = new ArrayList();
        datas.add(new NavBean("001", 1));
        mFragmentList.add(SoilFragment.newInstance());
        for (int i = 0; i < datas.size(); i++) {
            mFragmentList.add(SoilFragment.newInstance());
            mTitles.add(datas.get(i).getName());
        }
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentList));
        binding.tab.setViewPager(binding.viewPager, mTitles);
        binding.tab.onPageSelected(0);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

//    public void save(View view) {
//        UserBean userBean = new UserBean();
//        userBean.setType("123");
//        LocalDataManager.getInstance().saveLoginInfo(userBean);
//    }

//    public void read(View view) {
//        binding.mTvRead.setText(LocalDataManager.getInstance()
//                .getLoginInfo(UserBean.class).getType());
//    }
}