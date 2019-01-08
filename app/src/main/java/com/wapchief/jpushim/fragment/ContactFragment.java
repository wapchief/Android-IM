package com.wapchief.jpushim.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.activity.AddFriendsActivity;
import com.wapchief.jpushim.activity.PullMsgListActivity;
import com.wapchief.jpushim.activity.UserInfoActivity;
import com.wapchief.jpushim.adapter.FriendsAdapter;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.entity.UserStateListBean;
import com.wapchief.jpushim.framework.network.NetWorkManager;
import com.wapchief.jpushim.framework.utils.TimeUtils;
import com.wapchief.jpushim.view.EmptyView;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wapchief on 2017/7/18.
 * 通讯录
 */

public class ContactFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.fm_contact_rv)
    RecyclerView mFmContactRv;
    Unbinder unbinder;
    @BindView(R.id.fm_contact_no)
    TextView mFmContactNo;
    @BindView(R.id.fm_contact_msg)
    RelativeLayout mFmContactMsg;
    @BindView(R.id.fm_contact_refresh)
    SwipeRefreshLayout mFmContactRefresh;
    private FriendsAdapter mFriendsAdapter;
    private UserInfo info;
    private String[] listUserName = new String[]{"1000", "1006"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_contact, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        mFmContactRefresh.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFmContactRv.setLayoutManager(layoutManager);
        mFriendsAdapter = new FriendsAdapter(FriendsAdapter.EnumMessageType.FRIENDS);
        mFriendsAdapter.setOnLoadMoreListener(this, mFmContactRv);
//        mFriendsAdapter.
        //分割线
        mFmContactRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mFmContactRv.setAdapter(mFriendsAdapter);
//        initGetList();
        initItemOnClick();
        initGetList();
    }

    /*监听item*/
    private void initItemOnClick() {
        mFriendsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserInfo userInfo = (UserInfo) mFriendsAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("USERNAME", userInfo.getUserName());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
//        isFriendStateList(listUserName);
        super.onResume();
    }


    /*获取好友列表*/
    private void initGetList() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void gotResult(final int i, String s, List<UserInfo> list) {
                if (i == 0) {
                    LogUtils.e("Log:好友数" + list
                            .size());

                    info = list.get(i);
                    mFmContactNo.setVisibility(View.GONE);
                    mFmContactRv.setVisibility(View.VISIBLE);
                    if (list.size() <= 0) {
                        mFmContactRv.setVisibility(View.GONE);
                        mFmContactNo.setVisibility(View.VISIBLE);
                        mFriendsAdapter.setEmptyView(new EmptyView(getContext()));
                    }
                    Collections.reverse(list);
                    mFriendsAdapter.setNewData(list);
                    mFriendsAdapter.loadMoreEnd();
                } else {
                    mFmContactRv.setVisibility(View.GONE);
                    mFmContactNo.setVisibility(View.VISIBLE);
                }
                mFmContactRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fm_contact_no, R.id.fm_contact_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fm_contact_no:
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_contact_msg:
                startActivity(new Intent(getActivity(), PullMsgListActivity.class));
                break;
                default:break;
        }
    }

    UserStateListBean stateListBean;
    String state;

    /*批量获取好友在线状态*/
    public void isFriendStateList(String[] listUserName) {

        NetWorkManager.isFriendStateList(listUserName, new Callback<UserStateListBean>() {
            @Override
            public void onResponse(Call<UserStateListBean> call, Response<UserStateListBean> response) {
//                stateListBean = response.body();
//                Log.e("onStateList=====", response.body().username);
            }

            @Override
            public void onFailure(Call<UserStateListBean> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh() {
        initGetList();
    }
}
