package com.wapchief.jpushim.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.entity.DefaultUser;
import com.wapchief.jpushim.entity.MyMessage;
import com.wapchief.jpushim.framework.base.BaseAcivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jpush.im.android.tasks.GetChatMsgTaskMng;

/**
 * Created by wapchief on 2017/7/19.
 */

public class ChatMsgActivity extends BaseAcivity {
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;

    @BindView(R.id.title)
    LinearLayout mTitle;
    @BindView(R.id.msg_list)
    MessageList mMsgList;
    @BindView(R.id.chat_input)
    ChatInputView mChatInput;
    @BindView(R.id.chat_view)
    RelativeLayout mChatView;
    private SharedPrefHelper helper;

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    private Context mContext;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;
    private ImageLoader imageLoader;
    private ImageView imageView;
    @Override
    protected int setContentView() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        mContext = ChatMsgActivity.this;
        mData = getMessages();
        initKeyBoard();
        initTitleBar();
        initMsgAdapter();
        View view = View.inflate(mContext, R.layout.item_receive_photo, null);
        imageView = (ImageView) view.findViewById(R.id.aurora_iv_msgitem_avatar);
        mTitleBarBack.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
//        imageView=mMsgList.focusableViewAvailable();
        imageLoader.loadImage(mTitleBarBack,"http://upload.jianshu.io/users/upload_avatars/2858691/4db2d471c01c?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240");
    }
    //初始化消息列表
    private List<MyMessage> getMessages() {
        List<MyMessage> list = new ArrayList<>();
        Resources res = getResources();
        String[] messages = res.getStringArray(R.array.messages_array);
        for (int i = 0; i < messages.length; i++) {
            MyMessage message;
            if (i % 2 == 0) {
                message = new MyMessage(messages[i], IMessage.MessageType.RECEIVE_TEXT);
                message.setUserInfo(new DefaultUser(helper.getUserId(), "DeadPool", "R.drawable.ironman"));
            } else {
                message = new MyMessage(messages[i], IMessage.MessageType.SEND_TEXT);
                message.setUserInfo(new DefaultUser("1", "IronMan", "R.drawable.ironman"));
            }
            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            list.add(message);
        }
        Collections.reverse(list);
        return list;
    }
    //初始化adapter
    private void initMsgAdapter() {
        //加载头像图片的方法
            imageLoader=new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView imageView, String s) {
                Picasso.with(getApplicationContext())
                        .load(s)
                        .placeholder(R.drawable.icon_user)
                        .into(imageView);
            }

            @Override
            public void loadImage(ImageView imageView, String s) {
                //缩略图
                Picasso.with(getApplicationContext())
                        .load(s)
                        .placeholder(R.drawable.icon_user)
                        .into(imageView);
            }
        };

        /**
         * 1、Sender Id: 发送方 Id(唯一标识)。
         * 2、HoldersConfig，可以用这个对象来构造自定义消息的 ViewHolder 及布局界面。
         * 如果不自定义则使用默认的布局
         * 3、ImageLoader 的实例，用来展示头像。如果为空，将会隐藏头像。
         */
        final MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<MyMessage>(helper.getUserId(), holdersConfig, imageLoader);
        //单击消息事件，可以选择查看大图或者播放视频
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO
                        || message.getType() == IMessage.MessageType.SEND_VIDEO) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(mContext, MsgVideoActivity.class);
//                        intent.putExtra(MsgVideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
                    showToast(ChatMsgActivity.this,"点击了消息");
                }
            }
        });

        //长按消息
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
//                showToast(ChatMsgActivity.this,"长按:"+message.getMsgId());
//                mMsgList.getTranslationY()
                Log.e("yyyyyyy",""+mMsgList.getScaleY()+","+mMsgList.getTranslationY());
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setItems(new String[]{"复制","转发","删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showToast(ChatMsgActivity.this, "" + i);

                            }
                        }).create();
                dialog.show();
                Window window = dialog.getWindow();
                WindowManager.LayoutParams params = window.getAttributes();
//                window.setGravity(Gravity.RIGHT | Gravity.TOP);
                params.width = UIUtils.dip2px(mContext,120);
                params.y = UIUtils.dip2px(mContext,55);
                dialog.getWindow().setAttributes(params);

//                message.
                // do something
            }
        });

        //点击头像
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Intent intent = new Intent(mContext, UserActivty.class);
                startActivity(intent);
            }
        });

        //重新发送
        mAdapter.setMsgResendListener(new MsgListAdapter.OnMsgResendListener<MyMessage>() {
            @Override
            public void onMessageResend(MyMessage message) {
                // resend message here
            }
        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message.setUserInfo(new DefaultUser("0", "Deadpool", "R.drawable.deadpool"));

        mAdapter.addToEnd(mData);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount <= mData.size()) {
                    Log.i("MessageListActivity", "Loading next page");
//                    loadNextPage();
                }
            }
        });

        mMsgList.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
    }


    /*加载更多*/
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }
    private void initKeyBoard() {
//        statusBarHeight = getStatusBarHeight(getApplicationContext());


        mChatView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        Log.e("height======", "" + keyboardHeight);
        mChatInput.setMenuContainerHeight(keyboardHeight);

    }
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            Rect r = new Rect();
            mChatView.getWindowVisibleDisplayFrame(r);

            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = mChatView.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);

            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            if(keyboardHeight == 0 && heightDiff > statusBarHeight){
                keyboardHeight = heightDiff - statusBarHeight;
            }

            if (isShowKeyboard) {
                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (heightDiff <= statusBarHeight) {
                    isShowKeyboard = false;
                    onHideKeyboard();
                }
            } else {
                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                // 说明这时软键盘已经弹出
                if (heightDiff > statusBarHeight) {
                    isShowKeyboard = true;
                    onShowKeyboard();
                }
            }
        }
    };

    private void onShowKeyboard() {
        // 在这里处理软键盘弹出的回调
        Log.e("onShowKeyboard : key = " ,""+ keyboardHeight);
//        mChatInput.setMenuContainerHeight(60);


    }

    private void onHideKeyboard() {
        // 在这里处理软键盘收回的回调
        Log.e("onHidekey = ","dd");
        mChatInput.setMenuContainerHeight(0);
    }

    /*标题栏*/
    private void initTitleBar() {
        mTitleBarBack.setVisibility(View.INVISIBLE);
        mTitleOptionsImg.setVisibility(View.GONE);
        mTitleOptionsTv.setVisibility(View.VISIBLE);
        mTitleBarTitle.setText(helper.getUserId());
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
}
