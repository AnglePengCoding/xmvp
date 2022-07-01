package com.github.anglepengcoding.mvp.rx_bus;


import com.github.anglepengcoding.mvp.utils.Logg;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * Created by 刘红鹏 on 2022/2/16.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 */
public class RxManager {

    public RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();// 管理订阅者者


    public void on(String eventName, Consumer<Object> action1) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, Throwable::printStackTrace));
    }

    public void add(Disposable m) {
        mCompositeSubscription.add(m);
        Logg.d("订阅 size------->" + mCompositeSubscription.size());
    }

    public void clear() {
        Logg.d("订阅 clear------->" + mCompositeSubscription.size());
        mCompositeSubscription.clear();// 取消订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

}