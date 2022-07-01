package com.github.anglepengcoding.mvp.utils.bar.barparams;

import android.database.ContentObserver;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

/**
 * Created by 刘红鹏 on 2022/2/14.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
 public class BarParams implements Cloneable {
    @ColorInt
    public int statusBarColor = Color.TRANSPARENT;
    @ColorInt
    public int navigationBarColor = Color.BLACK;
    @FloatRange(from = 0f, to = 1f)
    public float statusBarAlpha = 0.0f;
    @FloatRange(from = 0f, to = 1f)
    public float navigationBarAlpha = 0.0f;
    public boolean fullScreen = false;
    public boolean fullScreenTemp = fullScreen;
    public BarHide barHide = BarHide.FLAG_SHOW_BAR;//隐藏Bar
    public boolean darkFont = false;
    public boolean statusBarFlag = true;
    @ColorInt
    public int statusBarColorTransform = Color.BLACK;
    @ColorInt
    public int navigationBarColorTransform = Color.BLACK;
    public Map<View, Map<Integer, Integer>> viewMap = new HashMap<>();
    @FloatRange(from = 0f, to = 1f)
    public float viewAlpha = 0.0f;
    public boolean fits = false;
    @ColorInt
    public int statusBarColorContentView = Color.TRANSPARENT;
    @ColorInt
    public int statusBarColorContentViewTransform = Color.BLACK;
    @FloatRange(from = 0f, to = 1f)
    public float statusBarContentViewAlpha = 0.0f;
    public int navigationBarColorTemp = navigationBarColor;
    public View statusBarView;
    public View navigationBarView;
    public View statusBarViewByHeight;
    @ColorInt
    public int flymeOSStatusBarFontColor;
    public boolean isSupportActionBar = false;
    public View titleBarView;
    public int titleBarHeight;
    public int titleBarPaddingTopHeight;
    public View titleBarViewMarginTop;
    public boolean titleBarViewMarginTopFlag = false;
    public boolean keyboardEnable = false;
    public int keyboardMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    public boolean navigationBarEnable = true;
    public boolean navigationBarWithKitkatEnable = true;

    @Deprecated
    public boolean fixMarginAtBottom = false;
    public boolean systemWindows = false;
    public KeyboardPatch keyboardPatch;
    public OnKeyboardListener onKeyboardListener;
    public ContentObserver navigationStatusObserver;

    @Override
    protected BarParams clone() {
        BarParams barParams = null;
        try {
            barParams = (BarParams) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return barParams;
    }


    public enum BarHide {
        FLAG_HIDE_STATUS_BAR, //隐藏状态栏
        FLAG_HIDE_NAVIGATION_BAR, //隐藏导航栏
        FLAG_HIDE_BAR,  //隐藏状态栏和导航栏
        FLAG_SHOW_BAR  //显示状态栏和导航栏
    }
}
