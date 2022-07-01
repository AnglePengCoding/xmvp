package com.github.anglepengcoding.mvp.base;

import android.content.Context;


import com.github.anglepengcoding.mvp.rx_bus.RxManager;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by 刘红鹏 on 2022/2/15.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public abstract class BasePresenter<M, T> {
    public M mModel;
    public T mView;
    public Context mContext;
    public RxManager mRxManager = new RxManager();

    public void attachVM(T v, M m, Context c) {
        this.mView = v;
        mContext = c;
        this.mModel = m;
        this.onStart();
        mRxManager.add(Disposable.disposed());
    }

    public void detachVM() {
        mRxManager.clear();
        mView = null;
        mModel = null;
        mContext = null;
    }


    public abstract void onStart();
}
