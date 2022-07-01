package com.github.anglepengcoding.xmvp;


import com.github.anglepengcoding.mvp.base.BaseModel;
import com.github.anglepengcoding.mvp.base.BasePresenter;
import com.github.anglepengcoding.mvp.base.BaseResponse;
import com.github.anglepengcoding.mvp.base.BaseView;

import io.reactivex.rxjava3.core.Observable;


/**
 * Created by 刘红鹏 on 2022/2/15.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public interface MainContract {
    interface Model extends BaseModel {
        Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(String eid);
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void sweepCode(String eid);
    }
}
