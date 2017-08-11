package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wapchief on 2017/7/21.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;

    @Override
    protected int setContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("关于");
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

    @OnClick(R.id.title_bar_back)
    public void onViewClicked() {
        finish();
    }
}
