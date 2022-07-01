package com.github.anglepengcoding.mvp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by 刘红鹏 on 2022/2/22.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class Prefs {

    private static Context appContext;
    private static String fileName;

    /**
     * Initialize this class for subsequent calls.
     * @param context A valid context object.
     * @param file The SP file name to read and write.
     */
    public static void init(@NonNull Context context, @NonNull String file){
        appContext = context.getApplicationContext();
        fileName = file;
    }

    /**
     * Retrieve sp value, assume the second parameter is the right class.
     * @param <T> Boolean, Integer, Long, Float, Double, String allowed. For other types, an
     *           UnsupportedOperationException is thrown.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull String key, T fallback) throws
            UnsupportedOperationException {
        SharedPreferences sp = getSharedPreferences();
        Object result;
        if (fallback instanceof Boolean){
            result = sp.getBoolean(key, (Boolean)fallback);
        }
        else if (fallback instanceof String){
            result = sp.getString(key, (String) fallback);
        }
        else if (fallback instanceof Integer){
            result = sp.getInt(key, (Integer) fallback);
        }
        else if (fallback instanceof Float){
            result = sp.getFloat(key, (Float) fallback);
        }
        else if (fallback instanceof Long) {
            result = sp.getLong(key, (Long) fallback);
        }
        else{
            throw new UnsupportedOperationException("Type not supported: " + fallback.getClass()
                    .getSimpleName());
        }
        return (T)result;
    }

    /**
     * Retrieve a String value from the preferences, default is an empty string.
     * 从偏好检索字符串值，默认为空字符串。
     */
    public static String getString(@NonNull String key){
        return get(key, "");
    }

    /**
     * Retrieve a long value from the preferences, default is <code>0</code>.
     */
    public static long getLong(@NonNull String key){
        return get(key, 0L);
    }

    /**
     * Retrieve a integer value from the preferences, default is <code>0</code>.
     */
    public static int getInt(@NonNull String key){
        return get(key, 0);
    }

    /**
     * Retrieve a boolean value from the preferences, default is <code>false</code>.
     */
    public static boolean getBoolean(@NonNull String key){
        return get(key, false);
    }

    /**
     * Put value into sp.
     * @param <T> Boolean, Integer, Long, Float, Double, String allowed. For other types, an
     *           UnsupportedOperationException is thrown.
     */
    public static <T> void set(@NonNull String key, @NonNull T value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof String){
            editor.putString(key, (String) value);
        }
        else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }
        else if (value instanceof Float){
            editor.putFloat(key, (Float) value);
        }
        else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        else{
            throw new UnsupportedOperationException("Type not supported: " + value.getClass()
                    .getSimpleName());
        }
        //use apply instead of commit to improve performance on UI thread.
        editor.apply();
    }

    public static void remove(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }

    /**
     * clear all data!
     * @see SharedPreferences.Editor#clear()
     */
    public static void clear(){
        getSharedPreferences().edit().clear().apply();
    }

    private static SharedPreferences getSharedPreferences() {
        checkInitiatedOrThrow();
        return appContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * Retrieve all values from the preferences.
     * @see SharedPreferences#getAll()
     */
    public static Map<String, ?> getAll(){
        return getSharedPreferences().getAll();
    }

    private static void checkInitiatedOrThrow() {
        if (appContext == null || TextUtils.isEmpty(fileName)) {
            throw new IllegalStateException("The Prefs class is not initialized correctly.");
        }
    }

}
