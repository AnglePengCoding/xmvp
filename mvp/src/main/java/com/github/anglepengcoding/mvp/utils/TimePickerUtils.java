package com.github.anglepengcoding.mvp.utils;

import android.content.Context;

import com.bigkoo.pickerview.builder.TimePickerBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by 刘红鹏 on 2022/3/14.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 * 时间选择工具
 */
public final class TimePickerUtils {

    public static void TimePickerShow(Context context, TimeSelectData selectListener) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 0, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        new TimePickerBuilder(context, (date, v) -> {
            if (selectListener != null) {
                selectListener.callback(getDateStr(date, null));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(14)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setDate(calendar)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, calendar)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setDecorView(((AppCompatActivity) context).getWindow().getDecorView().findViewById(android.R.id.content)).build().show();

    }

    public static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd ";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public interface TimeSelectData {
        void callback(String data);
    }
}
