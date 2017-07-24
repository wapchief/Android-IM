package com.wapchief.jpushim.view;

import android.util.Log;
import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import cn.jpush.im.android.api.JMessageClient;

import static cn.jpush.im.android.api.JMessageClient.NOTI_MODE_SILENCE;

/**
 * Created by wapchief on 2017/7/21.
 */

public class MyCompoundButton implements CompoundButton.OnCheckedChangeListener {

    public int select;
    public int noselect;
    SharedPrefHelper helper=SharedPrefHelper.getInstance();
    public MyCompoundButton(int select, int noselect) {
        this.select = select;
        this.noselect = noselect;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.e("bbbbbbbbb=", ":" + b);
        if (b==false){
            JMessageClient.setNotificationFlag(noselect);
            if (noselect==NOTI_MODE_SILENCE){
            }
        }else {
            JMessageClient.setNotificationFlag(select);
        }
    }
}
