package com.github.anglepengcoding.mvp.base;


import android.graphics.Color;
import android.text.TextUtils;

import com.github.anglepengcoding.mvp.net.ResultException;
import com.github.anglepengcoding.mvp.utils.Logg;
import com.github.anglepengcoding.mvp.utils.dialog.ProgressDialogUtils;
import com.github.anglepengcoding.mvp.utils.net.NetworkUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.yk.loading.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by 刘红鹏 on 2022/2/17.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public abstract class BaseObserver<E extends BaseResponse> implements Observer<E> {
    protected final String LOG_TAG = getClass().getSimpleName();
    private final BaseUiInterface mUiInterface;
    private LoadingDialog dialog;

    public BaseObserver(BaseUiInterface baseUiInterface) {
        mUiInterface = baseUiInterface;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!isNetConnect()) {
            d.dispose();
            mUiInterface.showNoNetworkConnection();
            return;
        }
        showLoadingDialog();
    }

    private boolean isNetConnect() {
        return NetworkUtils.isConnected();
    }

    private void showLoadingDialog() {
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(mUiInterface.getContext())
                .setMessage("加载中...")//设置提示文字
                .setCancelable(true)//按返回键取消
                .setMessageColor(Color.WHITE)//提示文字颜色
                .setMessageSize(14)//提示文字字号
                .setBackgroundTransparent(true)//弹窗背景色是透明或半透明
                .setCancelOutside(true);//点击空白区域弹消失
        dialog = loadBuilder.create();
        dialog.show();
    }

    @Override
    public void onComplete() {
        dialog.dismiss();
    }

    @Override
    public void onNext(@NonNull E response) {
        try {
            switch (getResultCode()) {
                case BaseResponse.RESULT_CODE_OPERATION_SUCCESSFUL:
                    onSuccess(response);
                    break;
            }
        } catch (ClassCastException c) {
            throw new ClassCastException("接收体异常");
        }

    }

    private String getResultCode() {
        if (TextUtils.isEmpty(BaseApp.getInstance().setResultCodeOperationSuccessful())) {
            return "200";
        }
        return BaseApp.getInstance().setResultCodeOperationSuccessful();
    }

    public abstract void onSuccess(E response);

    @Override
    public void onError(@NonNull Throwable throwable) {
        handleError(throwable, mUiInterface, LOG_TAG);
        dialog.dismiss();
    }


    public void handleError(Throwable throwable, BaseUiInterface mUiInterface, String LOG_TAG) {
        if (throwable == null) {
            mUiInterface.showUnknownException();
            return;
        }
        //分为以下几类问题：网络连接，数据解析，客户端出错【空指针等】，服务器内部错误
        if (throwable instanceof SocketTimeoutException || throwable
                instanceof ConnectException || throwable instanceof UnknownHostException) {
            mUiInterface.showNoNetworkConnection();
        } else if ((throwable instanceof JsonSyntaxException) || (throwable instanceof
                NumberFormatException) || (throwable instanceof MalformedJsonException)) {
            mUiInterface.showDataException("数据解析出错");
        } else if ((throwable instanceof HttpException)) {
            mUiInterface.showDataException("服务器错误(" + ((HttpException) throwable).code() + ")");
        } else if (throwable instanceof NullPointerException) {
            mUiInterface.showDataException(BaseUiInterface.ERROR_CODE);
        } else if (throwable instanceof ResultException) {
            onServerErrorShow(((ResultException) throwable));
        } else {
            Logg.e("throwable:00-" + throwable.getMessage());
            mUiInterface.showDataException("客户端开小差了，攻城狮正在修复中...");
        }
    }

    private void onServerErrorShow(ResultException throwable) {
        try {
            switch (throwable.getCode()) {
                case BaseResponse.RESULT_CODE_OPERATION_FAILED:
                case BaseResponse.RESULT_CODE_NOT_PERMISSION:
                case BaseResponse.RESULT_CODE_ILLEGAL_REQUEST:
                case BaseResponse.RESULT_CODE_NOT_LOGGED:
                case BaseResponse.RESULT_CODE_LOGIN_TIME_OUT:
                case BaseResponse.RESULT_CODE_SERVER_BUSY:
                case BaseResponse.RESULT_CODE_PARAMETER_EXCEPTION:
                default:
                    mUiInterface.showDataException(throwable.getMsg());
            }
        } catch (ClassCastException c) {
            throw new ClassCastException("接收体异常");
        }
    }
}