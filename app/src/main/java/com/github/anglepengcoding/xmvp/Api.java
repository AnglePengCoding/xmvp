package com.github.anglepengcoding.xmvp;



import com.github.anglepengcoding.mvp.base.BaseResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 刘红鹏 on 2022/2/17.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */

public interface Api {
    //扫合格证
    @GET("qCertPrint/sweepCode")
    Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(@Query("id") String eid);

}
