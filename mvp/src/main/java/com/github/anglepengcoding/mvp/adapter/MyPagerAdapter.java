package com.github.anglepengcoding.mvp.adapter;


import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by 刘红鹏 on 2021/9/24.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */

public class MyPagerAdapter extends PagerAdapter {


    private List<View> mViewList;
    private List<String> titles;

    public MyPagerAdapter(List<View> mViewList, List<String> titles) {
        this.mViewList = mViewList;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        //返回有效的View的个数
        return mViewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
