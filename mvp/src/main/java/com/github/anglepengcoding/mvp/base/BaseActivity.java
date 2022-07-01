package com.github.anglepengcoding.mvp.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.github.anglepengcoding.mvp.databinding.ActivityBaseBinding;
import com.github.anglepengcoding.mvp.utils.AppManager;
import com.github.anglepengcoding.mvp.utils.Logg;
import com.github.anglepengcoding.mvp.utils.TUtil;
import com.github.anglepengcoding.mvp.utils.bar.StatusBar;
import com.github.anglepengcoding.mvp.utils.net.NetworkUtils;
import com.github.anglepengcoding.mvp.utils.permission.PermissionUtils;
import com.luck.picture.lib.utils.ToastUtils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import me.jessyan.autosize.internal.CustomAdapt;

import static android.content.ContentValues.TAG;


/**
 * Created by 刘红鹏 on 2022/2/14.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public abstract class BaseActivity<T extends ViewBinding,
        P extends BasePresenter, M extends BaseModel> extends AppCompatActivity
        implements BaseUiInterface, CustomAdapt {

    protected StatusBar mBar;
    protected T binding;
    public P mPresenter;
    public M mModel;
    protected Context mContext;

    /** 标题栏默认隐藏   baseBinding.mRlTitleLayout.setVisibility(View.VISIBLE);  */
    /**
     * 设置标题栏可以使用baseBinding  baseBinding.mTitleText.setText("");
     */
    protected ActivityBaseBinding baseBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "--->Activity onCreate()<---");
        mBar = StatusBar.with(this);
        mBar.transparentStatusBar()
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .init();
        parseIntentData(getIntent(), false);//得到传递信息
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initBinding();
        mContext = this;
        mPresenter = TUtil.getT(this, 1);
        mModel = TUtil.getT(this, 2);
        if (this instanceof BaseView) mPresenter.attachVM(this, mModel, mContext);
        initView();
        initPresenter();
        setListeners();
        AppManager.getAppManager().addActivity(this);
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
     * 得到传递信息
     *
     * @param intent
     * @param isFromNewIntent
     */
    protected void parseIntentData(Intent intent, boolean isFromNewIntent) {
        //empty implementation
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "--->Activity onNewIntent()<---");
        parseIntentData(intent, true);
    }

    /**
     * 设置监听
     */
    protected void setListeners() {
        try {
            baseBinding.leftImageView.setOnClickListener(view1 -> {
                finish();
            });
        } catch (Exception e) {
            Logg.e("baseBinding.leftImageView not found");
        }
    }

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度进行适配, {@code false} 为按照高度进行适配
     */

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    /**
     * 这里使用 iPhone 的设计图, iPhone 的设计图尺寸为 750px * 1334px, 高换算成 dp 为 667 (1334px / 2 = 667dp)
     * <p>
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
    @Override
    public float getSizeInDp() {
        return 375;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "--->Activity onStart()<---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "--->Activity onRestart()<---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--->Activity onResume()<---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "--->Activity onPause()<---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "--->Activity onDestroy()<---");
        if (mBar != null) mBar.destroy();
        if (mPresenter != null) mPresenter.detachVM();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            //如果不是落在EditText区域，则需要关闭输入法
            if (HideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean HideKeyboard(View view, MotionEvent event) {
        if ((view instanceof EditText)) {

            int[] location = {0, 0};
            view.getLocationInWindow(location);
            //获取现在拥有焦点的控件view的位置，即EditText
            int left = location[0], top = location[1], bottom = top + view.getHeight(), right = left + view.getWidth();
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            boolean isInEt = (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom);
            return !isInEt;
        }
        return false;
    }


    /**
     * @param tarActivity 快速跳转
     */
    public void startActivity(Class<? extends AppCompatActivity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    /**
     * String类型
     *
     * @param tarActivity 携带数据跳转
     */
    public void startStringExActivity(Class<? extends AppCompatActivity> tarActivity, String name, String value) {
        Intent intent = new Intent(this, tarActivity);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * 申请运行权限
     *
     * @param callback    权限回调
     * @param permissions 运行权限
     */
    public void requestRunPermission(final PermissionUtils.FullCallback callback, final String... permissions) {
        PermissionUtils permissionUtils = PermissionUtils.permission(permissions);
        permissionUtils.request();
        permissionUtils.callback(callback);
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

    @Override
    public void showUnknownException() {
        showToast("未知错误，请稍后重试");
    }


    /**
     * 无网络显示
     */
    @Override
    public void showNoNetworkConnection() {
        if (!NetworkUtils.isConnected()) {
            showToast("请检查手机网络连接");
        } else {
            showToast("访问地址出错");
        }
    }

    /**
     * 快速打印Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @NonNull
    @Override
    public Activity getAActivity() {
        return this;
    }

    private void initBinding() {
        baseBinding = ActivityBaseBinding.inflate(getLayoutInflater());
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, Boolean.TYPE);
            binding = (T) method.invoke(null, getLayoutInflater(), baseBinding.mFrameLayout, true);
            setContentView(baseBinding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
