package com.github.anglepengcoding.mvp.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * 程序自启动
 */
public abstract class BaseReceiver extends BroadcastReceiver {
    /**
     * 需要跳转的界面
     * @return
     */
    public abstract Class<?> getIntentClass();

    /**
     * 多长时间
     * @return
     */
    public abstract long delayMillis();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            new Handler().postDelayed(() -> {
                Intent thisIntent = new Intent(context, getIntentClass());
                thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(thisIntent);
            }, delayMillis());
        }
    }
}
