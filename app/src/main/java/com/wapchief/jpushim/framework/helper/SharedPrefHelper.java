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

    /*昵称*/
    public void setNakeName(String guestId) {
        sharedPreferences.edit().putString("userNakeName", guestId).commit();
    }

    public String getNakeName() {
        return sharedPreferences.getString("userNakeName", "");
    }

    /*漫游开启状态*/
    public void setRoaming(boolean flag) {
        sharedPreferences.edit().putBoolean("roaming", flag).commit();
    }

    public boolean getRoaming() {
        return sharedPreferences.getBoolean("roaming", false);
    }
    /*推送开启状态*/
    public void setPush(boolean flag) {
        sharedPreferences.edit().putBoolean("push", flag).commit();
    }

    public boolean getMusic() {
        return sharedPreferences.getBoolean("push_music", false);
    }

    /*声音推送开启状态*/
    public void setMusic(boolean flag) {
        sharedPreferences.edit().putBoolean("push_music", flag).commit();
    }

    public boolean getPush() {
        return sharedPreferences.getBoolean("push", false);
    }
    /*震动开启状态*/
    public void setVib(boolean flag) {
        sharedPreferences.edit().putBoolean("push_vib", flag).commit();
    }

    public boolean getVib() {
        return sharedPreferences.getBoolean("push_vib", false);
    }

    public void setAppKey(String appKey) {
        sharedPreferences.edit().putString("appkey", appKey).commit();
    }
    public String getAppKey() {
        return sharedPreferences.getString("appkey","");
    }
}
