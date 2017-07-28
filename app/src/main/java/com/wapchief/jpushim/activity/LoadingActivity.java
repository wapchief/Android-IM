package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Handler;

import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/7/19.
 */

public class LoadingActivity extends BaseActivity {
    private SharedPrefHelper helper;
    @Override
    protected int setContentView() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        final Handler handler = new Handler();
        // getUserMessage();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (helper.getUserPW().equals("")) {
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    JMessageClient.login(helper.getUserId(), helper.getUserPW(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i==0){
                                initUserInfo();
                                startActivity(new Intent(getApplication(), MainActivity.class));
                                LoadingActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoadingActivity.this,LoadingActivity.class));
                                showToast(LoadingActivity.this, "登陆失败:"+s);
                            }
                        }
                    });

                }

            }
        }, 500);
    }

    @Override
    protected void initData() {

    }

    public void initUserInfo(){
        JMessageClient.getUserInfo(helper.getUserId(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i==0) {
                    helper.setNakeName(userInfo.getNickname());
                    helper.setUserId(userInfo.getUserName());
                }
            }
        });
    }
}
