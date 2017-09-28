package com.wapchief.jpushim.entity;

import java.util.List;

/**
 * Created by wapchief on 2017/9/26.
 */

public class UserStateListBean {


    /**
     * devices : [{"login":false,"online":false,"platform":"a"}]
     * username : Rauly
     */

    public String username;
    public List<DevicesBean> devices;

    public static class DevicesBean {
        /**
         * login : false
         * online : false
         * platform : a
         */

        public boolean login;
        public boolean online;
        public String platform;
    }
}
