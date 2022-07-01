package com.github.anglepengcoding.mvp.utils;

import android.text.TextUtils;

public class CheckUtils {
    //    /**
//     * 验证手机格式
//     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String num = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    //验证邮箱
    public static boolean isEmail(String email){
        String mail="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (null==email || "".equals(email)) return false;
        return email.matches(mail);
    }


    // 验证密码，至少为字母、数字、符号两种组成，不包含空格,不能输入中文
    public static boolean isPassword(String password){
        String pw="^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-zA-Z]|[0-9]){6,}$";
        if (null==password || "".equals(password)) return false;
        return password.matches(pw);
    }

}
