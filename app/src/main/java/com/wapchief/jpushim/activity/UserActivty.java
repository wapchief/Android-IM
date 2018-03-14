package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Build;
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
import com.wapchief.jpushim.framework.utils.StringUtils;
import com.wapchief.jpushim.framework.utils.TimeUtils;

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

    @Override
    protected int setContentView() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        initBar();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initBar() {
        mTitleBarTitle.setText("个人资料");
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));

        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setText("编辑个人资料");
    }

    @Override
    protected void initData() {
        mUserinfoNikename.setText(JMessageClient.getMyInfo().getNickname() + "");
        mUserinfoBirthday.setText(TimeUtils.ms2date("yyyy-MM-dd", JMessageClient.getMyInfo().getBirthday()));
        mUserinfoGender.setText(StringUtils.constant2String(JMessageClient.getMyInfo().getGender().name()));
        if (StringUtils.isNull(JMessageClient.getMyInfo().getSignature())) {
            mUserinfoSignature.setText("签名：暂未设置签名");
        } else {
            mUserinfoSignature.setText(JMessageClient.getMyInfo().getSignature() + "");
        }
//        Log.e("info====", JMessageClient.getMyInfo().getAddress() + "\nregion:" + info.getRegion());
        mUserinfoRegion.setText(JMessageClient.getMyInfo().getAddress() + "");
        mUserinfoUsername.setText(JMessageClient.getMyInfo().getUserName() + "");
        mUserinfoMtime.setText("上次更新：" + TimeUtils.unix2Date("yyyy-MM-dd hh-mm", JMessageClient.getMyInfo().getmTime()));
        Picasso.with(this)
                .load(JMessageClient.getMyInfo().getAvatarFile())
                .placeholder(R.mipmap.icon_user)
                .into(mUserinfoAvatar);

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
                Intent intent = new Intent(this, UserEditActivity.class);
                startActivity(intent);
                break;
        }
    }
}
