package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by wapchief on 2017/7/26.
 * 请求列表
 */

public class PullMsgListActivity extends BaseActivity {

    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.pull_msg_rv)
    RecyclerView mPullMsgRv;
    @BindView(R.id.pull_msg_rv2)
    RecyclerView mPullMsgRv2;
    @BindView(R.id.pull_msg_push)
    TextView mPullMsgPush;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    private MessageRecyclerAdapter adapter, adapter2;
    List<MessageBean> list2 = new ArrayList<>();
    private int TYPE_BUTTON = 0;
    private MessageBean bean;
    private SharedPrefHelper helper;

    @Override
    protected int setContentView() {
        return R.layout.activity_pull_msg;
    }

    @Override
    protected void initView() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("新的好友");
        mTitleOptionsTv.setText("添加");
        mTitleOptionsTv.setVisibility(View.VISIBLE);
        helper = SharedPrefHelper.getInstance();
        initAdapter();
        initAdapter2();
    }

    private void initAdapter2() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mPullMsgRv2.setLayoutManager(layoutManager);
        adapter2 = new MessageRecyclerAdapter(list2, this);
        //分割线
        mPullMsgRv2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mPullMsgRv2.setAdapter(adapter2);
        final int[] id = {1001, 1002, 1003};
        initDataAdapter(id);
        /*item监听事件*/
        adapter2.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initDataAdapter(int[] id) {
        for (int i = 0; i < id.length; i++) {
//            list2.clear();
            dataAdapter2(id[i]);
        }
    }

    /*加载推荐好友数据*/
    private void dataAdapter2(final int id) {
        JMessageClient.getUserInfo(id + "", "", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                bean = new MessageBean();
                Log.e("userinfoMsg", s + " ," + userInfo.getUserID() + "  ," + userInfo.getNickname());
                bean.setTitle(userInfo.getNickname());
                bean.setContent(userInfo.getUserName() + "");
                bean.setType(1);
                list2.add(bean);
                Log.e("bean1===", bean.getTitle() + "  ," + bean.getContent());
                TYPE_BUTTON = 1;
                adapter2.notifyDataSetChanged();

            }

        });
    }

    private void initAdapter() {
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

    @OnClick({R.id.title_bar_back, R.id.title_options_tv, R.id.pull_msg_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.title_options_tv:
                startActivity(new Intent(this, AddFriendsActivity.class));
                break;
            case R.id.pull_msg_push:
                startActivity(new Intent(this, AddFriendsActivity.class));
                break;
        }
    }

}
