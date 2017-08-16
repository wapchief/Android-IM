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
import com.wapchief.jpushim.framework.helper.GreenDaoHelper;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.greendao.RequestListDao;
import com.wapchief.jpushim.greendao.model.RequestList;

import java.util.ArrayList;
import java.util.Collections;
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
    @BindView(R.id.pull_msg_more)
    TextView mPullMsgMore;
    private MessageRecyclerAdapter adapter, adapter2;
    List<MessageBean> list2 = new ArrayList<>();
    private int TYPE_BUTTON = 0;
    private MessageBean bean, bean1;
    private SharedPrefHelper helper;

    private GreenDaoHelper daoHelper;
    private RequestListDao dao;
    private List<RequestList> list = new ArrayList<>();
    private List<MessageBean> list1 = new ArrayList<>();

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
        adapter = new MessageRecyclerAdapter(list1, this);
        adapter2 = new MessageRecyclerAdapter(list2, this);
        initAdapter2();

    }

    @Override
    protected void onResume() {
        list.clear();
        list1.clear();
        adapter.clear();
        initAdapter();
        super.onResume();
    }

    private void initAdapter2() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mPullMsgRv2.setLayoutManager(layoutManager);
        //分割线
        mPullMsgRv2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mPullMsgRv2.setAdapter(adapter2);
        final int[] id = {1000, 1001, 1006};
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
            dataAdapter2(id[i]);
            Collections.reverse(list2);
        }
//        Log.e("list2=====", ""+list2);
    }

    /*加载推荐好友数据*/
    private void dataAdapter2(final int id) {
        JMessageClient.getUserInfo(id + "", "", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                bean = new MessageBean();
//                Log.e("userinfoMsg", ""+userInfo);
                bean.setTitle(userInfo.getNickname());
                bean.setContent(userInfo.getUserName() + "");
                bean.setUserName(userInfo.getUserName());
                bean.setType(1);
                bean.setFriends(userInfo.isFriend());
                bean.setImg(userInfo.getAvatarFile().toURI().toString());
//                Log.e("bean1===", bean.getTitle() + "  ," + bean.getContent());
                TYPE_BUTTON = 1;
                list2.add(bean);
                adapter2.notifyDataSetChanged();

            }

        });
    }

    /*好友申请*/
    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mPullMsgRv.setLayoutManager(layoutManager);
        //分割线
        mPullMsgRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mPullMsgRv.setAdapter(adapter);

        daoHelper = new GreenDaoHelper(this);
        dao = daoHelper.initDao().getRequestListDao();
        //查询所有
        list = dao.queryBuilder().list();
        Collections.reverse(list);
        //这里用于判断是否有数据
        if (list.size() == 0) {
            mPullMsgRv.setVisibility(View.GONE);
        } else {
            mPullMsgRv.setVisibility(View.VISIBLE);
        }
        int size = list.size();
        if (size > 3) {
            for (int i = 0; i < 3; i++) {
                bean1 = new MessageBean();
                bean1.setType(2);
                bean1.setTitle(list.get(i).getNakeName());
                bean1.setContent("验证信息:" + list.get(i).getMsg());
                bean1.setUserName(list.get(i).getUserName());
                bean1.setImg(list.get(i).getImg());
                list1.add(bean1);
            }
        } else {
            for (int i = 0; i < size; i++) {
                bean1 = new MessageBean();
                bean1.setType(2);
                bean1.setTitle(list.get(i).getNakeName());
                bean1.setContent("验证信息:" + list.get(i).getMsg());
                bean1.setUserName(list.get(i).getUserName());
                bean1.setImg(list.get(i).getImg());
                list1.add(bean1);
            }
            Collections.reverse(list1);
        }
        //list倒序排列
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
//                JMessageClient.registerEventReceiver(this);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_bar_back, R.id.title_options_tv, R.id.pull_msg_push,R.id.pull_msg_more})
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
            case R.id.pull_msg_more:
                startActivity(new Intent(this,PullMsgListMoreActivity.class));
                break;
        }
    }

}
