package com.github.anglepengcoding.xmvp.fragment;

import com.github.anglepengcoding.mvp.base.BaseObserver;
import com.github.anglepengcoding.mvp.base.BaseResponse;
import com.github.anglepengcoding.xmvp.QualifiedEntCertInfo2;

public class SoilPresenter extends SoilContract.Presenter {
    @Override
    public void onStart() {
        sweepCode("111");
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
