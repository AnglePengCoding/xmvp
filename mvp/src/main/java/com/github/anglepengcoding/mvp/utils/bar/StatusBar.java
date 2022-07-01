package com.github.anglepengcoding.mvp.utils.bar;

import android.app.Activity;
import android.app.Dialog;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.github.anglepengcoding.mvp.utils.bar.barparams.BarConfig;
import com.github.anglepengcoding.mvp.utils.bar.barparams.BarParams;
import com.github.anglepengcoding.mvp.utils.bar.barparams.FlymeOSStatusBarFontUtils;
import com.github.anglepengcoding.mvp.utils.bar.barparams.KeyboardPatch;
import com.github.anglepengcoding.mvp.utils.bar.barparams.OSUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

/**
 * Created by 刘红鹏 on 2022/2/14.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class StatusBar {

    private static Map<String, BarParams> mMap = new HashMap<>();


    private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";

    private Activity mActivity;
    private Window mWindow;
    private String mActivityName;
    private String mFragmentName;
    private String mStatusBarName;

    private ViewGroup mDecorView;
    private ViewGroup mContentView;

    private BarConfig mConfig;
    private BarParams mBarParams;


    private Dialog mDialog;

    private StatusBar(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    public static StatusBar with(@NonNull Activity activity) {
        return new StatusBar(activity);
    }

    public static StatusBar with(@NonNull Fragment fragment) {
        return new StatusBar(fragment);
    }

    /**
     * 在dialog里使用
     * With immersion bar.
     *
     * @param activity  the activity
     * @param dialog    the dialog
     * @param dialogTag the dialog tag
     * @return the immersion bar
     */
    public static StatusBar with(@NonNull Activity activity, @NonNull Dialog dialog, @NonNull String dialogTag) {
        if (isEmpty(dialogTag))
            throw new IllegalArgumentException("tag不能为null或空");
        return new StatusBar(activity, dialog, dialogTag);
    }

    private StatusBar(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        mActivity = activityWeakReference.get();
        mWindow = mActivity.getWindow();
        mActivityName = activity.getClass().getName();
        mStatusBarName = mActivityName;
        initParams();
    }

    private StatusBar(Activity activity, Fragment fragment) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        WeakReference<Fragment> fragmentWeakReference = new WeakReference<>(fragment);
        mActivity = activityWeakReference.get();
        mWindow = mActivity.getWindow();
        mActivityName = mActivity.getClass().getName();
        mFragmentName = mActivityName + "_AND_" + fragmentWeakReference.get().getClass().getName();
        mStatusBarName = mFragmentName;
        initParams();
    }

    /**
     * 在Dialog里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity  the activity
     * @param dialog    the dialog
     * @param dialogTag the dialog tag  dialog标识，不能为空
     */
    private StatusBar(Activity activity, Dialog dialog, String dialogTag) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        WeakReference<Dialog> dialogWeakReference = new WeakReference<>(dialog);
        mActivity = activityWeakReference.get();
        mDialog = dialogWeakReference.get();
        mWindow = mDialog.getWindow();
        mActivityName = mActivity.getClass().getName();
        mStatusBarName = mActivityName + "_AND_" + dialogTag;
        initParams();
    }
    /**
     * 初始化沉浸式默认参数
     * Init params.
     */
    private void initParams() {
        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = (ViewGroup) mDecorView.findViewById(android.R.id.content);
        mConfig = new BarConfig(mActivity);
        if (mMap.get(mStatusBarName) == null) {
            mBarParams = new BarParams();
            if (!isEmpty(mFragmentName)) { //保证一个activity页面有同一个状态栏view和导航栏view
                if (mMap.get(mActivityName) == null)
                    throw new IllegalArgumentException("在Fragment里使用时，请先在加载Fragment的Activity里初始化！！！");
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                        || OSUtils.isEMUI3_1()) {
                    mBarParams.statusBarView = mMap.get(mActivityName).statusBarView;
                    mBarParams.navigationBarView = mMap.get(mActivityName).navigationBarView;
                }
                mBarParams.keyboardPatch = mMap.get(mActivityName).keyboardPatch;
            }
            mMap.put(mStatusBarName, mBarParams);
        } else {
            mBarParams = mMap.get(mStatusBarName);
        }
    }

    /**
     * 透明状态栏，默认透明
     *
     * @return the immersion bar
     */
    public StatusBar transparentStatusBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        return this;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public BarParams getBarParams() {
        return mBarParams;
    }


    /**
     * 通过状态栏高度动态设置状态栏布局
     */
    private void setStatusBarView() {
        if (mBarParams.statusBarViewByHeight != null) {
            ViewGroup.LayoutParams params = mBarParams.statusBarViewByHeight.getLayoutParams();
            params.height = mConfig.getStatusBarHeight();
            mBarParams.statusBarViewByHeight.setLayoutParams(params);
        }
    }

    /**
     * 变色view
     * <p>
     * Transform view.
     */
    private void transformView() {
        if (mBarParams.viewMap.size() != 0) {
            Set<Map.Entry<View, Map<Integer, Integer>>> entrySet = mBarParams.viewMap.entrySet();
            for (Map.Entry<View, Map<Integer, Integer>> entry : entrySet) {
                View view = entry.getKey();
                Map<Integer, Integer> map = entry.getValue();
                Integer colorBefore = mBarParams.statusBarColor;
                Integer colorAfter = mBarParams.statusBarColorTransform;
                for (Map.Entry<Integer, Integer> integerEntry : map.entrySet()) {
                    colorBefore = integerEntry.getKey();
                    colorAfter = integerEntry.getValue();
                }
                if (view != null) {
                    if (Math.abs(mBarParams.viewAlpha - 0.0f) == 0)
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.statusBarAlpha));
                    else
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.viewAlpha));
                }
            }
        }
    }

    /**
     * 解决底部输入框与软键盘问题
     * Keyboard enable.
     */
    private void keyboardEnable() {
        if (mBarParams.keyboardPatch == null) {
            mBarParams.keyboardPatch = KeyboardPatch.patch(mActivity, mWindow);
        }
        mBarParams.keyboardPatch.setBarParams(mBarParams);
        if (mBarParams.keyboardEnable) {  //解决软键盘与底部输入框冲突问题
            mBarParams.keyboardPatch.enable(mBarParams.keyboardMode);
        } else {
            mBarParams.keyboardPatch.disable(mBarParams.keyboardMode);
        }
    }

    /**
     * 注册emui3.x导航栏监听函数
     * Register emui 3 x.
     */
    private void registerEMUI3_x() {
        if ((OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) && mConfig.hasNavigtionBar()
                && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (mBarParams.navigationStatusObserver == null && mBarParams.navigationBarView != null) {
                mBarParams.navigationStatusObserver = new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        int navigationBarIsMin = Settings.System.getInt(mActivity.getContentResolver(),
                                NAVIGATIONBAR_IS_MIN, 0);
                        if (navigationBarIsMin == 1) {
                            //导航键隐藏了
                            mBarParams.navigationBarView.setVisibility(View.GONE);
                            mContentView.setPadding(0, mContentView.getPaddingTop(), 0, 0);
                        } else {
                            //导航键显示了
                            mBarParams.navigationBarView.setVisibility(View.VISIBLE);
                            if (!mBarParams.systemWindows) {
                                if (mConfig.isNavigationAtBottom())
                                    mContentView.setPadding(0, mContentView.getPaddingTop(), 0, mConfig.getNavigationBarHeight());
                                else
                                    mContentView.setPadding(0, mContentView.getPaddingTop(), mConfig.getNavigationBarWidth(), 0);
                            } else
                                mContentView.setPadding(0, mContentView.getPaddingTop(), 0, 0);
                        }
                    }
                };
            }
            mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor
                    (NAVIGATIONBAR_IS_MIN), true, mBarParams.navigationStatusObserver);
        }
    }

    /**
     * 取消注册emui3.x导航栏监听函数
     * Un register emui 3 x.
     */
    private void unRegisterEMUI3_x() {
        if ((OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) && mConfig.hasNavigtionBar()
                && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (mBarParams.navigationStatusObserver != null && mBarParams.navigationBarView != null)
                mActivity.getContentResolver().unregisterContentObserver(mBarParams.navigationStatusObserver);
        }
    }

    private int initBarAboveLOLLIPOP(int uiFlags) {
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;  //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION; //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (mConfig.hasNavigtionBar()) {  //判断是否存在导航栏
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
        if (mBarParams.statusBarFlag)
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));  //设置状态栏颜色
        else
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));  //设置状态栏颜色
        if (mBarParams.navigationBarEnable)
            mWindow.setNavigationBarColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                    mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));  //设置导航栏颜色
        return uiFlags;
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private int setStatusBarDarkFont(int uiFlags) {
        if (mBarParams.darkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 支持actionBar的界面
     * Support action bar.
     */
    private void supportActionBar() {
        if (!OSUtils.isEMUI3_1()) {
            for (int i = 0, count = mContentView.getChildCount(); i < count; i++) {
                View childView = mContentView.getChildAt(i);
                if (childView instanceof ViewGroup) {
                    mBarParams.systemWindows = childView.getFitsSystemWindows();
                    if (mBarParams.systemWindows) {
                        mContentView.setPadding(0, 0, 0, 0);
                        return;
                    }
                }
            }
            if (mBarParams.isSupportActionBar) {
                mContentView.setPadding(0, mConfig.getStatusBarHeight() + mConfig.getActionBarHeight(), 0, 0);
            } else {
                if (mBarParams.fits)
                    mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                else
                    mContentView.setPadding(0, 0, 0, 0);
            }
        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
        if (mBarParams.statusBarView == null) {
            mBarParams.statusBarView = new View(mActivity);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        mBarParams.statusBarView.setLayoutParams(params);
        if (mBarParams.statusBarFlag)
            mBarParams.statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        else
            mBarParams.statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        mBarParams.statusBarView.setVisibility(View.VISIBLE);
        ViewGroup viewGroup = (ViewGroup) mBarParams.statusBarView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mBarParams.statusBarView);
        mDecorView.addView(mBarParams.statusBarView);
    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private void setupNavBarView() {
        if (mBarParams.navigationBarView == null) {
            mBarParams.navigationBarView = new View(mActivity);
        }
        FrameLayout.LayoutParams params;
        if (mConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new FrameLayout.LayoutParams(mConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
        }
        mBarParams.navigationBarView.setLayoutParams(params);
        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (!mBarParams.fullScreen && (mBarParams.navigationBarColorTransform == Color.TRANSPARENT)) {
                mBarParams.navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                        Color.BLACK, mBarParams.navigationBarAlpha));
            } else {
                mBarParams.navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                        mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));
            }
        } else
            mBarParams.navigationBarView.setBackgroundColor(Color.TRANSPARENT);
        mBarParams.navigationBarView.setVisibility(View.VISIBLE);
        ViewGroup viewGroup = (ViewGroup) mBarParams.navigationBarView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mBarParams.navigationBarView);
        mDecorView.addView(mBarParams.navigationBarView);
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private void initBarBelowLOLLIPOP() {
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        setupStatusBarView(); //创建一个假的状态栏
        if (mConfig.hasNavigtionBar()) {  //判断是否存在导航栏，是否禁止设置导航栏
            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
            else
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setupNavBarView();   //创建一个假的导航栏
        }
    }

    /**
     * 解决安卓4.4和EMUI3.1导航栏与状态栏的问题，以及系统属性fitsSystemWindows的坑
     */
    private void solveNavigation() {
        for (int i = 0, count = mContentView.getChildCount(); i < count; i++) {
            View childView = mContentView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (childView instanceof DrawerLayout) {
                    View childAt1 = ((DrawerLayout) childView).getChildAt(0);
                    if (childAt1 != null) {
                        mBarParams.systemWindows = childAt1.getFitsSystemWindows();
                        if (mBarParams.systemWindows) {
                            mContentView.setPadding(0, 0, 0, 0);
                            return;
                        }
                    }
                } else {
                    mBarParams.systemWindows = childView.getFitsSystemWindows();
                    if (mBarParams.systemWindows) {
                        mContentView.setPadding(0, 0, 0, 0);
                        return;
                    }
                }
            }

        }
        // 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题
        if (mConfig.hasNavigtionBar() && !mBarParams.fullScreenTemp && !mBarParams.fullScreen) {
            if (mConfig.isNavigationAtBottom()) { //判断导航栏是否在底部
                if (!mBarParams.isSupportActionBar) { //判断是否支持actionBar
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(),
                                    0, mConfig.getNavigationBarHeight()); //有导航栏，获得rootView的根节点，然后设置距离底部的padding值为导航栏的高度值
                        else
                            mContentView.setPadding(0, 0, 0, mConfig.getNavigationBarHeight());
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                        else
                            mContentView.setPadding(0, 0, 0, 0);
                    }
                } else {
                    //支持有actionBar的界面
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, mConfig.getNavigationBarHeight());
                    else
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, 0);
                }
            } else {
                if (!mBarParams.isSupportActionBar) {
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(),
                                    mConfig.getNavigationBarWidth(), 0); //不在底部，设置距离右边的padding值为导航栏的宽度值
                        else
                            mContentView.setPadding(0, 0, mConfig.getNavigationBarWidth(), 0);
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                        else
                            mContentView.setPadding(0, 0, 0, 0);
                    }
                } else {
                    //支持有actionBar的界面
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, mConfig.getNavigationBarWidth(), 0);
                    else
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, 0);
                }
            }
        } else {
            if (!mBarParams.isSupportActionBar) {
                if (mBarParams.fits)
                    mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                else
                    mContentView.setPadding(0, 0, 0, 0);
            } else {
                //支持有actionBar的界面
                mContentView.setPadding(0, mConfig.getStatusBarHeight() + mConfig.getActionBarHeight() + 10, 0, 0);
            }
        }
    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */

    private int hideBar(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (mBarParams.barHide) {
                case FLAG_HIDE_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.INVISIBLE;
                    break;
                case FLAG_HIDE_STATUS_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.INVISIBLE;
                    break;
                case FLAG_HIDE_NAVIGATION_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    break;
                case FLAG_SHOW_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
                    break;
            }
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @return boolean 成功执行返回true
     */
    private void setMIUIStatusBarDarkFont(Window window, boolean darkFont) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (darkFont) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化状态栏和导航栏
     */
    private void initBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //防止系统栏隐藏时内容区域大小发生变化
        if (!OSUtils.isEMUI3_1()) {
            uiFlags = initBarAboveLOLLIPOP(uiFlags); //初始化5.0以上，包含5.0
            uiFlags = setStatusBarDarkFont(uiFlags); //android 6.0以上设置状态栏字体为暗色
            supportActionBar();
        } else {
            initBarBelowLOLLIPOP(); //初始化5.0以下，4.4以上沉浸式
            solveNavigation();  //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
        }
        uiFlags = hideBar(uiFlags);  //隐藏状态栏或者导航栏
        mWindow.getDecorView().setSystemUiVisibility(uiFlags);
        if (OSUtils.isMIUI6Later())
            setMIUIStatusBarDarkFont(mWindow, mBarParams.darkFont);         //修改miui状态栏字体颜色
        if (OSUtils.isFlymeOS4Later()) {          // 修改Flyme OS状态栏字体颜色
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
            }
        }
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public StatusBar addViewSupportTransformColorInt(View view, @ColorInt int viewColorAfterTransform) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(mBarParams.statusBarColor, viewColorAfterTransform);
        mBarParams.viewMap.put(view, map);
        return this;
    }

    /**
     * 透明导航栏，默认黑色
     *
     * @return the immersion bar
     */
    public StatusBar transparentNavigationBar() {
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        mBarParams.fullScreen = true;
        return this;
    }


    /**
     * 透明状态栏和导航栏
     *
     * @return the immersion bar
     */
    public StatusBar transparentBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        mBarParams.fullScreen = true;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public StatusBar statusBarColor(@ColorRes int statusBarColor) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor));
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public StatusBar statusBarColorInt(@ColorInt int statusBarColor,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }


    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色，资源文件（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    public StatusBar statusBarColorInt(@ColorInt int statusBarColor,
                                       @ColorInt int statusBarColorTransform,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public StatusBar navigationBarColor(@ColorRes int navigationBarColor) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor));
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    public StatusBar navigationBarColor(@ColorRes int navigationBarColor,
                                        @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor), navigationAlpha);
    }

    public StatusBar navigationBarColor(@ColorRes int navigationBarColor,
                                        @ColorRes int navigationBarColorTransform,
                                        @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor),
                ContextCompat.getColor(mActivity, navigationBarColorTransform), navigationAlpha);
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public StatusBar navigationBarColor(String navigationBarColor) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor));
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public StatusBar navigationBarColorInt(@ColorInt int navigationBarColor) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    public StatusBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarAlpha = navigationAlpha;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    public StatusBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                           @ColorInt int navigationBarColorTransform,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarColorTransform = navigationBarColorTransform;
        mBarParams.navigationBarAlpha = navigationAlpha;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public StatusBar barColorInt(@ColorInt int barColor) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
        return this;
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public StatusBar statusBarColorTransformInt(@ColorInt int statusBarColorTransform) {
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public StatusBar statusBarColorInt(@ColorInt int statusBarColor) {
        mBarParams.statusBarColor = statusBarColor;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public StatusBar barColor(@ColorRes int barColor) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor));
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public StatusBar statusBarColorTransform(String statusBarColorTransform) {
        return this.statusBarColorTransformInt(Color.parseColor(statusBarColorTransform));
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view the view
     * @return the immersion bar
     */
    public StatusBar addViewSupportTransformColor(View view) {
        return this.addViewSupportTransformColorInt(view, mBarParams.statusBarColorTransform);
    }

    /**
     * view透明度
     * View alpha immersion bar.
     *
     * @param viewAlpha the view alpha
     * @return the immersion bar
     */
    public StatusBar viewAlpha(@FloatRange(from = 0f, to = 1f) float viewAlpha) {
        mBarParams.viewAlpha = viewAlpha;
        return this;
    }

    /**
     * Remove support view immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public StatusBar removeSupportView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        Map<Integer, Integer> map = mBarParams.viewMap.get(view);
        if (map.size() != 0) {
            mBarParams.viewMap.remove(view);
        }
        return this;
    }

    /**
     * Remove support all view immersion bar.
     *
     * @return the immersion bar
     */
    public StatusBar removeSupportAllView() {
        if (mBarParams.viewMap.size() != 0) {
            mBarParams.viewMap.clear();
        }
        return this;
    }

    /**
     * 有导航栏的情况下，Activity是否全屏显示
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    public StatusBar fullScreen(boolean isFullScreen) {
        mBarParams.fullScreen = isFullScreen;
        return this;
    }

    /**
     * 状态栏透明度
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    public StatusBar statusBarAlpha(@FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.statusBarAlpha = statusAlpha;
        return this;
    }

    /**
     * 导航栏透明度
     *
     * @param navigationAlpha the navigation alpha
     * @return the immersion bar
     */
    public StatusBar navigationBarAlpha(@FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * 状态栏和导航栏透明度
     *
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public StatusBar barAlpha(@FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    public StatusBar statusBarDarkFont(boolean isDarkFont) {
        return statusBarDarkFont(isDarkFont, 0f);
    }


    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public StatusBar flymeOSStatusBarFontColor(@ColorRes int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = ContextCompat.getColor(mActivity, flymeOSStatusBarFontColor);
        return this;
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public StatusBar flymeOSStatusBarFontColor(String flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = Color.parseColor(flymeOSStatusBarFontColor);
        return this;
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public StatusBar flymeOSStatusBarFontColorInt(@ColorInt int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor;
        return this;
    }

    /**
     * 隐藏导航栏或状态栏
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    public StatusBar hideBar(BarParams.BarHide barHide) {
        mBarParams.barHide = barHide;
        if (OSUtils.isEMUI3_1()) {
            if ((mBarParams.barHide == BarParams.BarHide.FLAG_HIDE_NAVIGATION_BAR) ||
                    (mBarParams.barHide == BarParams.BarHide.FLAG_HIDE_BAR)) {
                mBarParams.navigationBarColor = Color.TRANSPARENT;
                mBarParams.fullScreenTemp = true;
            } else {
                mBarParams.navigationBarColor = mBarParams.navigationBarColorTemp;
                mBarParams.fullScreenTemp = false;
            }
        }
        return this;
    }

    /**
     * 解决布局与状态栏重叠问题
     *
     * @param fits the fits
     * @return the immersion bar
     */
    public StatusBar fitsSystemWindows(boolean fits) {
        mBarParams.fits = fits;
        return this;
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                      the fits
     * @param statusBarColorContentView the status bar color content view  状态栏颜色
     * @return the immersion bar
     */
    public StatusBar fitsSystemWindows(boolean fits, @ColorRes int statusBarColorContentView) {
        return fitsSystemWindows(fits, statusBarColorContentView,
                android.R.color.black, 0);
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                               the fits
     * @param statusBarColorContentView          the status bar color content view 状态栏颜色
     * @param statusBarColorContentViewTransform the status bar color content view transform  状态栏变色后的颜色
     * @param statusBarContentViewAlpha          the status bar content view alpha  透明度
     * @return the immersion bar
     */
    public StatusBar fitsSystemWindows(boolean fits, @ColorRes int statusBarColorContentView
            , @ColorRes int statusBarColorContentViewTransform, @FloatRange(from = 0f, to = 1f) float statusBarContentViewAlpha) {
        mBarParams.fits = fits;
        mBarParams.statusBarColorContentView = ContextCompat.getColor(mActivity, statusBarColorContentView);
        mBarParams.statusBarColorContentViewTransform = ContextCompat.getColor(mActivity, statusBarColorContentViewTransform);
        mBarParams.statusBarContentViewAlpha = statusBarContentViewAlpha;
        mBarParams.statusBarColorContentView = ContextCompat.getColor(mActivity, statusBarColorContentView);
        mContentView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColorContentView,
                mBarParams.statusBarColorContentViewTransform, mBarParams.statusBarContentViewAlpha));
        return this;
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     *
     * @param view the view
     * @return the immersion bar
     */
    public StatusBar statusBarView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        mBarParams.statusBarViewByHeight = view;
        return this;
    }

    /**
     * 通过状态栏高度动态设置状态栏布局,只能在Activity中使用
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public StatusBar statusBarView(@IdRes int viewId) {
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("未找到viewId");
        }
        return statusBarView(view);
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     * Status bar view immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public StatusBar statusBarView(@IdRes int viewId, View rootView) {
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("未找到viewId");
        }
        return statusBarView(view);
    }

    /**
     * 支持有actionBar的界面,调用该方法，布局讲从actionBar下面开始绘制
     * Support action bar immersion bar.
     *
     * @param isSupportActionBar the is support action bar
     * @return the immersion bar
     */
    public StatusBar supportActionBar(boolean isSupportActionBar) {
        mBarParams.isSupportActionBar = isSupportActionBar;
        return this;
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public StatusBar titleBar(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        return titleBar(view, true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view          the view
     * @param statusBarFlag the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public StatusBar titleBar(View view, boolean statusBarFlag) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        mBarParams.titleBarView = view;
        mBarParams.statusBarFlag = statusBarFlag;
        setTitleBar();
        return this;
    }

    /**
     * 重新绘制标题栏高度，解决状态栏与顶部重叠问题
     * Sets title bar.
     */
    private void setTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mBarParams.titleBarView != null) {
            final ViewGroup.LayoutParams layoutParams = mBarParams.titleBarView.getLayoutParams();
            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                    layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                mBarParams.titleBarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBarParams.titleBarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (mBarParams.titleBarHeight == 0)
                            mBarParams.titleBarHeight = mBarParams.titleBarView.getHeight() + mConfig.getStatusBarHeight();
                        if (mBarParams.titleBarPaddingTopHeight == 0)
                            mBarParams.titleBarPaddingTopHeight = mBarParams.titleBarView.getPaddingTop()
                                    + mConfig.getStatusBarHeight();
                        layoutParams.height = mBarParams.titleBarHeight;
                        mBarParams.titleBarView.setPadding(mBarParams.titleBarView.getPaddingLeft(),
                                mBarParams.titleBarPaddingTopHeight,
                                mBarParams.titleBarView.getPaddingRight(),
                                mBarParams.titleBarView.getPaddingBottom());
                        mBarParams.titleBarView.setLayoutParams(layoutParams);
                    }
                });
            } else {
                if (mBarParams.titleBarHeight == 0)
                    mBarParams.titleBarHeight = layoutParams.height + mConfig.getStatusBarHeight();
                if (mBarParams.titleBarPaddingTopHeight == 0)
                    mBarParams.titleBarPaddingTopHeight = mBarParams.titleBarView.getPaddingTop()
                            + mConfig.getStatusBarHeight();
                layoutParams.height = mBarParams.titleBarHeight;
                mBarParams.titleBarView.setPadding(mBarParams.titleBarView.getPaddingLeft(),
                        mBarParams.titleBarPaddingTopHeight,
                        mBarParams.titleBarView.getPaddingRight(),
                        mBarParams.titleBarView.getPaddingBottom());
                mBarParams.titleBarView.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，只支持Activity
     * Title bar immersion bar.
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public StatusBar titleBar(@IdRes int viewId) {
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, true);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId        the view id
     * @param statusBarFlag the status bar flag
     * @return the immersion bar
     */
    public StatusBar titleBar(@IdRes int viewId, boolean statusBarFlag) {
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, statusBarFlag);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public StatusBar titleBar(@IdRes int viewId, View rootView) {
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，支持任何view
     * Title bar immersion bar.
     *
     * @param viewId        the view id
     * @param rootView      the root view
     * @param statusBarFlag the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public StatusBar titleBar(@IdRes int viewId, View rootView, boolean statusBarFlag) {
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, statusBarFlag);
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   标题栏资源id
     * @return the immersion bar
     */
    public StatusBar titleBarMarginTop(@IdRes int viewId) {
        return titleBarMarginTop(mActivity.findViewById(viewId));
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId   the view id  标题栏资源id
     * @param rootView the root view  布局view
     * @return the immersion bar
     */
    public StatusBar titleBarMarginTop(@IdRes int viewId, View rootView) {
        return titleBarMarginTop(rootView.findViewById(viewId));
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param view the view  要改变的标题栏view
     * @return the immersion bar
     */
    public StatusBar titleBarMarginTop(View view) {
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        mBarParams.titleBarViewMarginTop = view;
        if (!mBarParams.titleBarViewMarginTopFlag)
            setTitleBarMarginTop();
        return this;
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Sets title bar margin top.
     */
    private void setTitleBarMarginTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mBarParams.titleBarViewMarginTop.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + mConfig.getStatusBarHeight(),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
            mBarParams.titleBarViewMarginTopFlag = true;
        }
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarFlag the status bar flag
     * @return the immersion bar
     */
    public StatusBar statusBarColorTransformEnable(boolean statusBarFlag) {
        mBarParams.statusBarFlag = statusBarFlag;
        return this;
    }

    /**
     * 一键重置所有参数
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    public StatusBar reset() {
        BarParams barParamsTemp = mBarParams;
        mBarParams = new BarParams();
        if (OSUtils.isEMUI3_1()) {
            mBarParams.statusBarView = barParamsTemp.statusBarView;
            mBarParams.navigationBarView = barParamsTemp.navigationBarView;
        }
        mBarParams.keyboardPatch = barParamsTemp.keyboardPatch;
        mMap.put(mStatusBarName, mBarParams);
        return this;
    }

    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     * Keyboard enable immersion bar.
     *
     * @param enable the enable
     * @return the immersion bar
     */
    public StatusBar keyboardEnable(boolean enable) {
        return keyboardEnable(enable, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 修改键盘模式
     * Keyboard mode immersion bar.
     *
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public StatusBar keyboardMode(int keyboardMode) {
        mBarParams.keyboardMode = keyboardMode;
        return this;
    }

    /**
     * 是否可以修改导航栏颜色，默认为true
     * Navigation bar enable immersion bar.
     *
     * @param navigationBarEnable the enable
     * @return the immersion bar
     */
    public StatusBar navigationBarEnable(boolean navigationBarEnable) {
        mBarParams.navigationBarEnable = navigationBarEnable;
        return this;
    }

    /**
     * 是否可以修改4.4设备导航栏颜色，默认为true
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    public StatusBar navigationBarWithKitkatEnable(boolean navigationBarWithKitkatEnable) {
        mBarParams.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable;
        return this;
    }


    /**
     * 当xml里使用android:fitsSystemWindows="true"属性时，
     * 解决4.4和emui3.1手机底部有时会出现多余空白的问题 ，已过时，代码中没用的此处
     * Fix margin atbottom immersion bar.
     *
     * @param fixMarginAtBottom the fix margin atbottom
     * @return the immersion bar
     */
    @Deprecated
    public StatusBar fixMarginAtBottom(boolean fixMarginAtBottom) {
        mBarParams.fixMarginAtBottom = fixMarginAtBottom;
        return this;
    }


    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     *
     * @param enable       the enable
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public StatusBar keyboardEnable(boolean enable, int keyboardMode) {
        mBarParams.keyboardEnable = enable;
        mBarParams.keyboardMode = keyboardMode;
        return this;
    }

    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha 如果不支持状态栏字体变色可以使用statusAlpha来指定状态栏透明度，比如白色状态栏的时候可以用到
     * @return the immersion bar
     */
    public StatusBar statusBarDarkFont(boolean isDarkFont, @FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.darkFont = isDarkFont;
        if (!isDarkFont)
            mBarParams.flymeOSStatusBarFontColor = 0;
        if (isSupportStatusBarDarkFont()) {
            mBarParams.statusBarAlpha = 0;
        } else {
            mBarParams.statusBarAlpha = statusAlpha;
        }
        return this;
    }

    public static boolean isSupportStatusBarDarkFont() {
        if (OSUtils.isMIUI6Later() || OSUtils.isFlymeOS4Later()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            return true;
        } else
            return false;
    }

    /**
     * 单独设置标题栏的高度
     * Sets title bar.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBar(final Activity activity, final View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {  //解决状态栏高度为warp_content或match_parent问题
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    lp.height = view.getHeight() + getStatusBarHeight(activity);
                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                            view.getPaddingRight(), view.getPaddingBottom());
                }
            });
        } else {
            lp.height += getStatusBarHeight(activity);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getStatusBarHeight();
    }

    public void init() {
        mMap.put(mStatusBarName, mBarParams);
        initBar();   //初始化沉浸式
        setStatusBarView();  //通过状态栏高度动态设置状态栏布局
        transformView();  //变色view
//        keyboardEnable();  //解决软键盘与底部输入框冲突问题
//        registerEMUI3_x();  //解决华为emui3.1或者3.0导航栏手动隐藏的问题
    }

    /**
     * 当Activity/Fragment/Dialog关闭的时候调用
     */
    public void destroy() {
        unRegisterEMUI3_x();
        if (mBarParams.keyboardPatch != null) {
            mBarParams.keyboardPatch.disable(mBarParams.keyboardMode);  //取消监听
            mBarParams.keyboardPatch = null;
        }
        if (mDecorView != null)
            mDecorView = null;
        if (mContentView != null)
            mContentView = null;
        if (mConfig != null)
            mConfig = null;
        if (mWindow != null)
            mWindow = null;
        if (mDialog != null)
            mDialog = null;
        if (mActivity != null)
            mActivity = null;
        if (!isEmpty(mStatusBarName)) {
            if (mBarParams != null)
                mBarParams = null;
            mMap.remove(mStatusBarName);
        }
    }


}
