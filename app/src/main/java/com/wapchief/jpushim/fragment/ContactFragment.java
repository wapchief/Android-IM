package com.wapchief.jpushim.fragment;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.activity.AddFriendsActivity;
import com.wapchief.jpushim.activity.PullMsgListActivity;
import com.wapchief.jpushim.activity.UserInfoActivity;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by wapchief on 2017/7/18.
 * 通讯录
 */

public class ContactFragment extends Fragment {


    @BindView(R.id.fm_contact_rv)
    RecyclerView mFmContactRv;
    Unbinder unbinder;
    @BindView(R.id.fm_contact_no)
    TextView mFmContactNo;
    @BindView(R.id.fm_contact_msg)
    RelativeLayout mFmContactMsg;
    private List<MessageBean> data = new ArrayList<>();
    private MessageRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_contact, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFmContactRv.setLayoutManager(layoutManager);
        adapter = new MessageRecyclerAdapter(data, getActivity());
        //分割线
        mFmContactRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mFmContactRv.setAdapter(adapter);
        initGetList();
        initItemOnClick();
    }

    /*监听item*/
    private void initItemOnClick() {
        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Log.e("initItem", data.get(position).getType() + "");
                if (data.get(position).type == 3) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra("USERNAME", data.get(position).getUserName());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden)
            data.clear();
            initGetList();
        super.onHiddenChanged(hidden);
    }

    /*获取好友列表*/
    MessageBean bean;
    private void initGetList() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {

                if (i==0) {
                    Log.e("Log:好友数", i + "    ,s:" + s + "   ," + list
                            .size());
                    mFmContactNo.setVisibility(View.GONE);
                    mFmContactRv.setVisibility(View.VISIBLE);
                    for (int j=0;j<list.size();j++) {
                        bean= new MessageBean();
                        bean.setTitle(list.get(j).getNickname());
                        bean.setContent(list.get(j).getAvatar());
                        bean.setTime(list.get(j).getUserID() + "");
                        bean.setUserName(list.get(j).getUserName());
                        bean.setImg(list.get(j).getAvatarFile().toURI().toString());
                        bean.setType(3);
                        data.add(bean);
                        Collections.reverse(data);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    mFmContactRv.setVisibility(View.GONE);
                    mFmContactNo.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fm_contact_no,R.id.fm_contact_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fm_contact_no:
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_contact_msg:
                startActivity(new Intent(getActivity(),PullMsgListActivity.class));
                break;
        }
    }


}
