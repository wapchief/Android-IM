package com.wapchief.jpushim.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.activity.ChatMsgActivity;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.framework.utils.StringUtils;
import com.wapchief.jpushim.framework.utils.TimeUtils;
import com.wapchief.jpushim.view.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by wapchief on 2017/7/18.
 */

public class MessageFragment extends Fragment {

    @BindView(R.id.fragment_main_group)
    RelativeLayout mFragmentMainGroup;
    @BindView(R.id.fragment_main_none)
    TextView mFragmentMainNone;
    private List<MessageBean> data = new ArrayList<>();

    @BindView(R.id.fragment_main_rv)
    RecyclerView mFragmentMainRv;
    Unbinder unbinder;
    MessageRecyclerAdapter adapter;
    @BindView(R.id.fragment_main_header)
    RecyclerViewHeader mFragmentMainHeader;
    @BindView(R.id.item_main_img)
    ImageView mItemMainImg;
    @BindView(R.id.item_main_username)
    TextView mItemMainUsername;
    @BindView(R.id.item_main_content)
    TextView mItemMainContent;
    @BindView(R.id.item_main_time)
    TextView mItemMainTime;
    private int groupID=0;
    MessageBean bean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_main, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        initData();
//        initDataBean();
        initGroup();
        onClickItem();
    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden)
//    }

    @Override
    public void onResume() {
        super.onResume();
        data.clear();
        initDataBean();

    }

    /*监听item*/
    private void onClickItem() {
        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view != null) {
                    Intent intent = new Intent(getActivity(), ChatMsgActivity.class);
                    intent.putExtra("USERNAME", data.get(position).getUserName());
                    intent.putExtra("NAKENAME", data.get(position).getTitle());
                    intent.putExtra("MSGID", data.get(position).getMsgID());
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                String[] strings = {"删除会话"};
                MyAlertDialog dialog=new MyAlertDialog(getActivity(), strings,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0){
                                    if (JMessageClient.deleteSingleConversation(data.get(position).getMsgID())==true){
                                        Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                                        data.clear();

                                        initDataBean();
                                    }else {
                                        Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                dialog.initDialog();

            }
        });
    }

    /*群组*/
    private void initGroup() {
        mItemMainImg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_group));
        mItemMainUsername.setText("群助手");
        mItemMainUsername.setTextSize(16);
        mItemMainContent.setText("[有1个未读消息]");
        mItemMainContent.setTextColor(Color.parseColor("#E5955D"));
    }

    private void initData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFragmentMainRv.setLayoutManager(layoutManager);
        adapter = new MessageRecyclerAdapter(data, getActivity());
        //分割线
        mFragmentMainRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mFragmentMainRv.setAdapter(adapter);
        mFragmentMainHeader.attachTo(mFragmentMainRv);

    }

    private void initDataBean() {
        List<Conversation> list = new ArrayList<>();
        list = JMessageClient.getConversationList();
        Log.e("list====", list.size()+"");
        if (list.size()==0){
            mFragmentMainNone.setVisibility(View.VISIBLE);
            mFragmentMainRv.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            bean = new MessageBean();
            try {
                bean.setContent(((TextContent)(list.get(i).getLatestMessage()).getContent()).getText());
            }catch (Exception e){
                bean.setContent("最近没有消息！");
            }
            bean.setMsgID(list.get(i).getId());
            bean.setUserName(list.get(i).getTargetId());
            bean.setTitle(list.get(i).getTitle());
            bean.setTime(list.get(i).getUnReadMsgCnt()+"条未读");
            bean.setConversation(list.get(i));
//            Log.e("listConVer====", list.get(i).getAllMessage()+".");
            data.add(bean);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragment_main_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_main_group:
                Toast.makeText(getActivity(), "暂未开放，敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
