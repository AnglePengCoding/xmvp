package com.github.anglepengcoding.xmvp;


import com.github.anglepengcoding.mvp.receiver.BaseReceiver;

public class Boot extends BaseReceiver {

    @Override
    public Class<MainActivity> getIntentClass() {
        return MainActivity.class;
    }

    @Override
    public long delayMillis() {
        return 3000;
    }
}
