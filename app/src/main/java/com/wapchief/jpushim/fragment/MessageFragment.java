package com.wapchief.jpushim.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.wapchief.jpushim.activity.ChatMsgActivity;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.entity.MessageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wapchief on 2017/7/18.
 */

public class MessageFragment extends Fragment {

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
        initDataBean();
        initGroup();
        onClickItem();
    }
    /*监听item*/
    private void onClickItem() {
        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view!=null) {
                    Intent intent = new Intent(getActivity(), ChatMsgActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

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

        for (int i = 0; i < 10; i++) {
            MessageBean bean = new MessageBean();
            bean.setContent( "用户" + i);
            bean.setTitle("八怪不姓丑"+i);
            bean.setTime("下午11:"+i+i);
            data.add(bean);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
