package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wapchief on 2017/7/24.
 * 添加好友／群组
 */

public class AddFriendsActivity extends BaseActivity {
    @BindView(R.id.add_tab)
    SegmentTabLayout mAddTab;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.add_search)
    EditText mAddSearch;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.add_commit)
    Button mAddCommit;
    private String[] mTitles = {"加好友", "加群"};

    @Override
    protected int setContentView() {
        return R.layout.activity_add_friends;
    }

    @Override
    protected void initView() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleOptionsImg.setVisibility(View.GONE);
        initTab();

    }

    private void initTab() {
        mAddTab.setTabData(mTitles);
        mTitleBarTitle.setText("加好友");
        mAddTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                if (i == 0) {
                    mTitleBarTitle.setText("加好友");
                    mAddSearch.setHint("搜索好友");
                } else {
                    mTitleBarTitle.setText("加群");
                    mAddSearch.setHint("搜索群");
                }
            }

            @Override
            public void onTabReselect(int i) {

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

    @OnClick({R.id.title_bar_back, R.id.add_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.add_commit:
                break;
        }
    }
}
