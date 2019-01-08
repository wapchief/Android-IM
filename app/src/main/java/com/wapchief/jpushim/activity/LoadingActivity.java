package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/7/19.
 */

public class LoadingActivity extends AppCompatActivity {
    private SharedPrefHelper helper;

    private void initView() {
        helper = SharedPrefHelper.getInstance();
        final Handler handler = new Handler();
        // getUserMessage();
        JAnalyticsInterface.onPageStart(this,this.getClass().getCanonicalName());
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
                                ToastUtils.showShort("登陆成功");
                                initUserInfo();
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                LoadingActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoadingActivity.this,LoginActivity.class));
                                ToastUtils.showShort("登陆失败:"+s);
                            }
                        }
                    });

                }

            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JAnalyticsInterface.onPageEnd(this,this.getClass().getCanonicalName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        BarUtils.setNotificationBarVisibility(false);
        BarUtils.setStatusBarVisibility(this,false);
        initView();
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
