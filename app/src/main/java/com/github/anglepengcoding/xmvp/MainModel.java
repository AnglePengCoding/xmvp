package com.github.anglepengcoding.xmvp;


import com.github.anglepengcoding.mvp.base.BaseResponse;
import com.github.anglepengcoding.mvp.net.RxService;
import com.github.anglepengcoding.mvp.utils.RxUtil;

import io.reactivex.rxjava3.core.Observable;


/**
 * Created by 刘红鹏 on 2022/2/15.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class MainModel implements MainContract.Model {
    @Override
    public Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(String eid) {
        return RxService.createApi(Api.class).sweepCode(eid).compose(RxUtil.rxSchedulerHelper());
    }
}
