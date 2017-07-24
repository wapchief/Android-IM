package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Handler;

import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

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
                    startActivity(new Intent(getApplication(), MainActivity.class));
                    LoadingActivity.this.finish();
                }

            }
        }, 500);
    }

    @Override
    protected void initData() {

    }
}
