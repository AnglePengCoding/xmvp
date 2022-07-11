package com.github.anglepengcoding.xmvp.fragment;

import com.github.anglepengcoding.mvp.base.BaseModel;
import com.github.anglepengcoding.mvp.base.BasePresenter;
import com.github.anglepengcoding.mvp.base.BaseView;

public interface SoilContract {

    interface Model extends BaseModel {
    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
