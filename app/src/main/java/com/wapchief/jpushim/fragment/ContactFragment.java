package com.wapchief.jpushim.fragment;

import android.app.ProgressDialog;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.activity.AddFriendsActivity;
import com.wapchief.jpushim.adapter.MessageRecyclerAdapter;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.view.MyProgressDialog;

import java.util.ArrayList;
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
    }

    /*获取好友列表*/
    private void initGetList() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                Log.e("userinfolist", i + "    ,s:" + s + "   ," + list
                        .size());
                if (list.size() == 0) {
                    mFmContactNo.setVisibility(View.VISIBLE);
                    mFmContactRv.setVisibility(View.GONE);
                } else {
                    mFmContactRv.setVisibility(View.VISIBLE);
                    mFmContactNo.setVisibility(View.GONE);
                    MessageBean bean = new MessageBean();
                    bean.setTitle(list.get(i).getNickname());
                    bean.setContent(list.get(i).getAvatar());
                    bean.setTime(list.get(i).getUserID() + "");
                    data.add(bean);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fm_contact_no)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
        startActivity(intent);

    }


//    /*自定义消息的加载进度条*/
//    public void showProgressDialog(String msg) {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage(msg);
//        progressDialog.setCancelable(true);
//        progressDialog.setCanceledOnTouchOutside(false);
//        try {
//            progressDialog.show();
//        } catch (WindowManager.BadTokenException exception) {
//            exception.printStackTrace();
//        }
//    }


}
