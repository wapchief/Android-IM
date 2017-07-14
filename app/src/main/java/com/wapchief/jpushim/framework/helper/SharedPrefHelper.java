package com.wapchief.jpushim.framework.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.wapchief.jpushim.framework.base.BaseApplication;

/**
 * Created by wapchief on 2017/7/14.
 * 本地存储
 */

public class SharedPrefHelper {
    private static SharedPrefHelper sharedPrefHelper = null;
    private static SharedPreferences sharedPreferences;

    public static synchronized SharedPrefHelper getInstance() {
        if (null == sharedPrefHelper) {
            sharedPrefHelper = new SharedPrefHelper();
        }
        return sharedPrefHelper;
    }

    private SharedPrefHelper() {
        sharedPreferences = BaseApplication.baseApplication
                .getSharedPreferences("SPH_NAME", Context.MODE_PRIVATE);
    }



    public void setUserId(String guestId) {
        sharedPreferences.edit().putString("userName", guestId).commit();
    }

    public String getUserId() {
        return sharedPreferences.getString("userName", "");
    }
    public void setUserPW(String guestId) {
        sharedPreferences.edit().putString("userPW", guestId).commit();
    }

    public String getUserPW() {
        return sharedPreferences.getString("userPW", "");
    }

}
