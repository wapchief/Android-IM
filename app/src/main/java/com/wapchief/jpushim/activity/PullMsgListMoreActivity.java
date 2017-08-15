package com.wapchief.jpushim.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.GreenDaoHelper;
import com.wapchief.jpushim.greendao.RequestListDao;
import com.wapchief.jpushim.greendao.model.RequestList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wapchief on 2017/8/14.
 * 查看更多好友请求
 */

public class PullMsgListMoreActivity extends BaseActivity {

    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.list_more_rv)
    RecyclerView mListMoreRv;
    private List<RequestList> list = new ArrayList<>();
    private List<MessageBean> list1 = new ArrayList<>();
    private MessageRecyclerAdapter adapter;
    private GreenDaoHelper daoHelper;
    private RequestListDao dao;

    @Override
    protected int setContentView() {
        return R.layout.activity_more_msglist;
    }

    @Override
    protected void initView() {
        initBar();

    }

    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("好友通知");
    }

    @Override
    protected void initData() {
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mListMoreRv.setLayoutManager(layoutManager);
            adapter = new MessageRecyclerAdapter(list1, this);
            //分割线
            mListMoreRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mListMoreRv.setAdapter(adapter);

            daoHelper = new GreenDaoHelper(this);
            dao = daoHelper.initDao().getRequestListDao();
            //查询所有
            list = dao.queryBuilder().list();

            //这里用于判断是否有数据
            if (list.size() == 0) {
                mListMoreRv.setVisibility(View.GONE);
            } else {
                mListMoreRv.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < list.size(); i++) {
              MessageBean bean1 = new MessageBean();
                bean1.setType(2);
                bean1.setTitle(list.get(i).getNakeName());
                bean1.setContent("验证信息:" + list.get(i).getMsg());
                bean1.setUserName(list.get(i).getUserName());
                bean1.setImg(list.get(i).getImg());
                list1.add(bean1);
            }
            //list倒序排列
            Collections.reverse(list1);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_bar_back, R.id.title_options_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.title_options_tv:
                break;
        }
    }
}
