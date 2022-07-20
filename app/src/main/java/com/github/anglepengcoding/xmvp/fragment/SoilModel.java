package com.github.anglepengcoding.xmvp.fragment;

import com.github.anglepengcoding.mvp.base.BaseResponse;
import com.github.anglepengcoding.mvp.net.RxService;
import com.github.anglepengcoding.mvp.utils.RxUtil;
import com.github.anglepengcoding.xmvp.Api;
import com.github.anglepengcoding.xmvp.QualifiedEntCertInfo2;

import io.reactivex.rxjava3.core.Observable;

public class SoilModel implements SoilContract.Model{
    @Override
    public Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(String eid) {
        return RxService.createApi(Api.class).sweepCode(eid).compose(RxUtil.rxSchedulerHelper());
    }
}
