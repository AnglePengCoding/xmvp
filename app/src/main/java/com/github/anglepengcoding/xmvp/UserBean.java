package com.github.anglepengcoding.xmvp;


import java.io.Serializable;

/**
 * Created by 刘红鹏 on 2022/2/22.
 * <p>https://github.com/AnglePengCoding</p>
 * <p>https://blog.csdn.net/LIU_HONGPENG</p>
 */
public class UserBean implements Serializable {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
