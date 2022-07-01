package com.github.anglepengcoding.xmvp;


import com.github.anglepengcoding.mvp.base.BaseObserver;
import com.github.anglepengcoding.mvp.base.BaseResponse;

import io.reactivex.rxjava3.disposables.Disposable;


/**
 * Created by 刘红鹏 on 2022/2/15.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class MainPresenter extends MainContract.Presenter {
    @Override
    public void onStart() {
    }

    @Override
    void sweepCode(String eid) {
        mModel.sweepCode(eid).subscribe(new BaseObserver<BaseResponse
                <QualifiedEntCertInfo2>>(mView) {
            @Override
            public void onSuccess(BaseResponse<QualifiedEntCertInfo2> response) {
            }
        });
    }
}
