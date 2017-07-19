package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseAcivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by wapchief on 2017/7/14.
 */

public class UserActivty extends BaseAcivity {

    SharedPrefHelper helper;
    @BindView(R.id.user_name)
    TextView mUserName;

    @Override
    protected int setContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        showProgressDialog();
        JMessageClient.getUserInfo(helper.getUserId(), "", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                mUserName.setText(userInfo.getNickname());
                dismissProgressDialog();
                Log.e("userinfo=======:", userInfo.toString());
            }
        });
    }

    @Override
    protected void initData() {

    }

}
