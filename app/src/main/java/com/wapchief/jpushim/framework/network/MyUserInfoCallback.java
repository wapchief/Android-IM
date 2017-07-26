package com.wapchief.jpushim.framework.network;

import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by wapchief on 2017/7/25.
 */

public class MyUserInfoCallback extends GetUserInfoCallback{
    @Override
    public void gotResult(int i, String s, UserInfo userInfo) {

    }

    public void errorResult(){

    }
}
