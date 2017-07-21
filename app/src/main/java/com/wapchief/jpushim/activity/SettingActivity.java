package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by wapchief on 2017/7/21.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;

    @BindView(R.id.setting_push)
    SwitchButton mSettingPush;
    @BindView(R.id.setting_noisy)
    SwitchButton mSettingNoisy;
    @BindView(R.id.setting_roaming)
    SwitchButton mSettingRoaming;


    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initTitleBar();
        initSwitchOnClick();
    }

    private void initSwitchOnClick() {
//        if (JMessageClient.getNotificationFlag())
    }

    private void initTitleBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("设置");
        mTitleOptionsImg.setVisibility(View.GONE);
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
