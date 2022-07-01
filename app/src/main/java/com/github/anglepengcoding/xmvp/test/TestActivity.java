//package com.github.anglepengcoding.xmvp.test;
//
//import com.github.anglepengcoding.mvp.base.BaseActivity;
//import com.github.anglepengcoding.mvp.rx_bus.RxBus;
//import com.github.anglepengcoding.mvp.rx_bus.RxManager;
//
//import androidx.viewbinding.ViewBinding;
//import io.reactivex.rxjava3.functions.Consumer;
//
///**
// * Created by 刘红鹏 on 2022/2/22.
// * <p>https://github.com/AnglePengCoding</p>
// * <p>https://blog.csdn.net/LIU_HONGPENG</p>
// */
//public class TestActivity extends BaseActivity {
//    @Override
//    protected ViewBinding getViewBinding() {
//        return null;
//    }
//
//    @Override
//    public void initView() {
//
//        mPresenter.mRxManager.on("tag", o -> {
//            String msg = (String) o;
//
//        });
//
//        new RxManager().on("tag", o -> {
//            String msg = (String) o;
//        });
//    }
//
//
//}
