package com.github.anglepengcoding.xmvp.fragment;

import com.github.anglepengcoding.mvp.base.BaseModel;
import com.github.anglepengcoding.mvp.base.BasePresenter;
import com.github.anglepengcoding.mvp.base.BaseResponse;
import com.github.anglepengcoding.mvp.base.BaseView;
import com.github.anglepengcoding.xmvp.QualifiedEntCertInfo2;

import io.reactivex.rxjava3.core.Observable;

public interface SoilContract {

    interface Model extends BaseModel {
        Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(String eid);
    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void sweepCode(String eid);
    }
}
