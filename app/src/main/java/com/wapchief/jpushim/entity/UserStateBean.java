package com.wapchief.jpushim.entity;

/**
 * Created by wapchief on 2017/9/25.
 */

public class UserStateBean {

    /**
     * login : true
     * online : false
     */

    public boolean login;
    public boolean online;

    @Override
    public String toString() {
        return "UserStateBean{" +
                "login=" + login +
                ", online=" + online +
                '}';
    }
}
