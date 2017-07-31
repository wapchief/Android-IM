package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wapchief on 2017/7/14.
 */

public class UserActivty extends BaseActivity {

    SharedPrefHelper helper;
    @BindView(R.id.userinfo_avatar)
    CircleImageView mUserinfoAvatar;
    @BindView(R.id.userinfo_nikename)
    TextView mUserinfoNikename;
    @BindView(R.id.userinfo_signature)
    TextView mUserinfoSignature;
    @BindView(R.id.userinfo_username)
    TextView mUserinfoUsername;
    @BindView(R.id.userinfo_gender)
    TextView mUserinfoGender;
    @BindView(R.id.userinfo_birthday)
    TextView mUserinfoBirthday;
    @BindView(R.id.userinfo_region)
    TextView mUserinfoRegion;
    @BindView(R.id.userinfo_mtime)
    TextView mUserinfoMtime;
    @BindView(R.id.userinfo_scroll)
    ScrollView mUserinfoScroll;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_tv2)
    TextView mBottomBarTv2;
    @BindView(R.id.bottom_bar_right)
    RelativeLayout mBottomBarRight;
    private String avatar = "";

    @Override
    protected int setContentView() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        initBar();
        JMessageClient.getUserInfo(helper.getUserId(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                avatar = userInfo.getAvatar();
                mUserinfoBirthday.setText(userInfo.getBirthday()+"");
                mUserinfoGender.setText(userInfo.getGender()+"");
                mUserinfoNikename.setText(userInfo.getNickname()+"");
                mUserinfoUsername.setText(userInfo.getUserName()+"");
                mUserinfoMtime.setText(userInfo.getmTime()+"");
                mUserinfoRegion.setText(userInfo.getRegion()+"");
                mUserinfoSignature.setText(userInfo.getSignature()+"");
            }
        });
        try {
            Picasso.with(this)
                    .load(avatar)
                    .into(mUserinfoAvatar);
        }catch (Exception e){
            Log.e("eeeeeeee", e.getMessage());
        }

    }

    private void initBar() {
        mTitleBarTitle.setText("个人资料");
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));

        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setText("编辑个人资料");
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

    @OnClick({R.id.title_bar_back, R.id.bottom_bar_tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.bottom_bar_tv2:
                Intent intent=new Intent(this,EditUserInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
