package com.wapchief.jpushim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.framework.base.BaseActivity;

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
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.pull_msg_rv)
    RecyclerView mPullMsgRv;
    @BindView(R.id.pull_msg_rv2)
    RecyclerView mPullMsgRv2;
    private MessageRecyclerAdapter adapter,adapter2;
    List<MessageBean> list2=new ArrayList<>();
    Handler handler=new Handler();
    private int TYPE_BUTTON=0;
    private MessageBean bean;
    @Override
    protected int setContentView() {
        return R.layout.activity_pull_msg;
    }

    @Override
    protected void initView() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("新的好友");
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


//        View view = View.inflate(PullMsgListActivity.this, R.layout.item_main_message, null);
//        Button itemBt = (Button) view.findViewById(R.id.item_main_bt);
//        itemBt.setVisibility(View.VISIBLE);
        final int[] id = {1001, 1002};
        initDataAdapter(id);
        adapter2.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Button itemBt = (Button) view.findViewById(R.id.item_main_bt);
                itemBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast(PullMsgListActivity.this, "" + position);
                            Intent intent = new Intent(PullMsgListActivity.this, AddFriendMsgActivity.class);
                            intent.putExtra("ID", id[position]);
                            startActivity(intent);
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initDataAdapter(int[] id) {
        for (int i=0;i<id.length;i++){
            bean = new MessageBean();
            dataAdapter2(id[i]);
            list2.add(bean);
        }
        adapter2.notifyDataSetChanged();
    }

    private void dataAdapter2(int id) {
        JMessageClient.getUserInfo(id+"","",new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.e("userinfoMsg", s + " ," + userInfo.getUserID()+"  ,"+userInfo.getNickname());
                    bean.setTitle(userInfo.getNickname());
                    bean.setContent(userInfo.getUserID()+"");
                    bean.setType(1);
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
