package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;

/**
 * Created by wapchief on 2017/8/24.
 */

public class GroupCreateActivity extends BaseActivity{
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.create_group_name)
    EditText mCreateGroupName;
    @BindView(R.id.create_group_content)
    EditText mCreateGroupContent;
    @BindView(R.id.create_group_bt)
    Button mCreateGroupBt;

    @Override
    protected int setContentView() {
        return R.layout.activty_create_group;
    }

    @Override
    protected void initView() {

        mTitleBarTitle.setText("创建群组");
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
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

    @OnClick({R.id.title_bar_back, R.id.create_group_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:

                break;
            case R.id.create_group_bt:
                createGroup(mCreateGroupName.getText().toString(),mCreateGroupContent.getText().toString());
                break;
        }
    }

    /*创建群组*/
    private void createGroup(String name,String desc) {
        JMessageClient.createGroup(name, desc, new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if (i==0){
                    Intent intent = new Intent(GroupCreateActivity.this,ChatMsgGroupActivity.class);
                    intent.putExtra("GROUPID", l);
                    startActivity(intent);
                }else {
                    showToast(GroupCreateActivity.this,"创建失败："+s);
                }
            }
        });
    }

}
