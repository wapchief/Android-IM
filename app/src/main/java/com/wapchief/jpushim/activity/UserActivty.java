package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by wapchief on 2017/7/14.
 */

public class UserActivty extends BaseActivity {

    SharedPrefHelper helper;
    @BindView(R.id.user_id)
    TextView mUserId;
    @BindView(R.id.user_userName)
    TextView mUserUserName;
    @BindView(R.id.user_nickname)
    TextView mUserNickname;
    @BindView(R.id.user_birthday)
    TextView mUserBirthday;
    @BindView(R.id.user_gender)
    TextView mUserGender;

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
                mUserId.setText("用户ID："+userInfo.getUserID());
                mUserUserName.setText("用户名："+userInfo.getUserName());
                mUserNickname.setText("昵    称："+userInfo.getNickname());
                mUserBirthday.setText("生    日："+userInfo.getBirthday());
                mUserGender.setText("性    别："+userInfo.getGender());
                dismissProgressDialog();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
