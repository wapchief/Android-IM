package com.wapchief.jpushim.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/8/24.
 * 用户更多操作
 */

public class UserInfoOptionsActivity extends BaseActivity {
    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_right)
    RelativeLayout mBottomBarRight;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title_bar_right)
    RelativeLayout mTitleBarRight;
    @BindView(R.id.bottom_bar_tv2)
    TextView mBottomBarTv2;
    private UserInfo info;

    @Override
    protected int setContentView() {
        return R.layout.activity_userinfo_options;
    }

    @Override
    protected void initView() {
        initUserInfo(getIntent().getStringExtra("USERNAME"));
        initBar();
    }

    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("更多");
        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setBackground(getResources().getDrawable(R.drawable.shape_button_lines3));
        mBottomBarTv2.setText("删除好友");

    }

    private void initUserInfo(final String username) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                info = userInfo;
//                Log.e("userinfooptions", ""+userInfo);
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

    @OnClick({R.id.bottom_bar_tv2, R.id.bottom_bar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bottom_bar_tv2:
                new AlertDialog.Builder(UserInfoOptionsActivity.this).setTitle("好友删除提示：")
                        .setMessage("该操作会将好友删除，并且会清空会话")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("dialog", "确定");
                                info.removeFromFriendList(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i==0){
                                            showToast(UserInfoOptionsActivity.this,"删除成功");
                                            //同时删除会话
                                            JMessageClient.deleteSingleConversation(info.getUserName());
                                            Intent intent=new Intent(UserInfoOptionsActivity.this, MainActivity.class);
                                            intent.putExtra("REMOVEID", info.getUserName());
                                            startActivity(intent);

                                        }else {
                                            showToast(UserInfoOptionsActivity.this,"删除失败"+s);
                                        }
                                    }
                                });

                            }
                        }).show();
                break;
            case R.id.title_bar_back:
                finish();
                break;
        }
    }

}
