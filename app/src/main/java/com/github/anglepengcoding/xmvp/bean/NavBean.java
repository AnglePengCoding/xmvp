package com.github.anglepengcoding.xmvp.bean;

/**
 * Created by Yuang on 2018/6/25.
 * Summary:
 */
public class NavBean {

    /**
     * id : 1
     * name : 强档
     * addtime : 2018-06-04 10:20:38
     */

    private String name;
    private int id;
    public NavBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
