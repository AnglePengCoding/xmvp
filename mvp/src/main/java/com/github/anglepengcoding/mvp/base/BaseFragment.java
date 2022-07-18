package com.github.anglepengcoding.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.anglepengcoding.mvp.utils.Logg;
import com.github.anglepengcoding.mvp.utils.TUtil;
import com.github.anglepengcoding.mvp.utils.bar.StatusBar;
import com.github.anglepengcoding.mvp.utils.net.NetworkUtils;
import com.luck.picture.lib.utils.ToastUtils;
import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import io.reactivex.rxjava3.functions.Consumer;
import me.jessyan.autosize.internal.CustomAdapt;

import static android.content.ContentValues.TAG;

/**
 * Created by 刘红鹏 on 2022/2/21.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public abstract class BaseFragment<T extends ViewBinding,
        P extends BasePresenter, M extends BaseModel> extends Fragment
        implements BaseUiInterface, CustomAdapt {

    protected StatusBar mBar;
    protected T binding;
    public P mPresenter;
    public M mModel;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getRootView(container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "--->Fragment onViewCreated()<---");
        mPresenter = TUtil.getT(this, 1);
        mModel = TUtil.getT(this, 2);
        if (this instanceof BaseView) mPresenter.attachVM(this, mModel, mContext);
        initView();
        initPresenter();
        setListeners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "--->Fragment onCreate()<---");
    }


    /**
     * 设置显示标题栏，问题，程序启动准备的程序
     */
    public abstract void initView();

    /**
     * 网络请求方法，统一处理网络失败请求
     */
    public abstract void initPresenter();

    /**
     * 设置按钮监听
     */
    protected void setListeners() {
        //empty implementation
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "--->Fragment onStart()<---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "--->Fragment onResume()<---");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "--->Fragment onPause()<---");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "--->Fragment onStop()<---");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "--->Fragment onDestroy()<---");
        if (mBar != null) mBar.destroy();
        if (mPresenter != null) mPresenter.detachVM();
    }


    /**
     * @param tarActivity 快速跳转
     */
    public void startActivity(Class<? extends AppCompatActivity> tarActivity) {
        Intent intent = new Intent(mContext, tarActivity);
        startActivity(intent);
    }


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }

    /**
     * String类型
     *
     * @param tarActivity 携带数据跳转
     */
    public void startStringExActivity(Class<? extends AppCompatActivity> tarActivity, String name, String value) {
        Intent intent = new Intent(mContext, tarActivity);
        intent.putExtra(name, value);
        startActivity(intent);
    }


    @Override
    public void showNoNetworkConnection() {
        if (!NetworkUtils.isConnected()) {
            showToast("请检查手机网络连接");
        } else {
            showToast("访问地址出错");
        }
    }

    @Override
    public void showUnknownException() {
        showToast("未知错误，请稍后重试");
    }

    @Override
    public void showDataException(String msg) {
        if (TextUtils.equals(ERROR_CODE, msg)) {
            showToast(msg);
            Logg.w("showDataException:" + msg);
        } else {
            showToast(msg);
        }
    }

    /**
     * 快速打印Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtils.showToast(mContext, msg);
    }

    @NonNull
    @Override
    public Activity getAActivity() {
        return mActivity;
    }

    private View getRootView( ViewGroup container){
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);
            binding = (T) method.invoke(null, getLayoutInflater(),container,false);
        } catch (NoSuchMethodException | IllegalAccessException| InvocationTargetException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    public interface RequestPermissions {
        void granted(Permission p);

        void shouldShowRequestPermissionRationale(Permission p);

        void refuse(Permission p);
    }
}
