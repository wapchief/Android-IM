package com.wapchief.jpushim.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.entity.DefaultUser;
import com.wapchief.jpushim.entity.MyMessage;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.utils.StringUtils;
import com.wapchief.jpushim.framework.utils.TimeUtils;
import com.wapchief.jpushim.framework.utils.UIUtils;
import com.wapchief.jpushim.view.MyAlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_TEXT;

/**
 * Created by wapchief on 2017/7/19.
 */

public class ChatMsgActivity extends BaseActivity {
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
    LinearLayout mChatView;
    @BindView(R.id.chat_et)
    EditText mChatEt;
    @BindView(R.id.chat_send)
    Button mChatSend;
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
    private String userName = "";
    private String msgID = "";
    private int position;
    List<Message> messages;
    List<Conversation> conversations;
    Conversation conversation;
    @Override
    protected int setContentView() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        helper = SharedPrefHelper.getInstance();
        mContext = ChatMsgActivity.this;
//        initKeyBoard();
        conversations = JMessageClient.getConversationList();
        userName = getIntent().getStringExtra("USERNAME");
        msgID = getIntent().getStringExtra("MSGID");
        //position从上个页面传递的会话位置
        position= getIntent().getIntExtra("position",0);
        conversation = conversations.get(position);
        Log.e("conver", conversation.getAllMessage().size() + "   ," + conversation.getAllMessage());
        mData = getMessages();
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
        for (int i = 0; i < conversation.getAllMessage().size(); i++) {
            MyMessage message;
            if (conversation.getAllMessage().get(i).getDirect()== MessageDirect.send){
                message = new MyMessage(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), SEND_TEXT);
                message.setUserInfo(new DefaultUser(userName, "IronMan", "R.drawable.ironman"));
            }else {
                message = new MyMessage(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), IMessage.MessageType.RECEIVE_TEXT);
                message.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), "DeadPool", "R.drawable.ironman"));

            }
            message.setPosition(i);
            message.setMsgID(conversation.getAllMessage().get(i).getId());
            message.setTimeString(TimeUtils.ms2date("MM-dd HH:mm",conversation.getAllMessage().get(i).getCreateTime()));
            list.add(message);

        }
        Collections.reverse(list);
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData = getMessages();
    }

    //初始化adapter
    private void initMsgAdapter() {
        //加载头像图片的方法
        imageLoader = new ImageLoader() {
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
                    showToast(ChatMsgActivity.this, "点击了消息");
                }
            }
        });

        //长按消息
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(final MyMessage message) {
                Log.e("mymessage", "id:" + mMsgList.getId()
                        + "\nposition:" +message.getPosition()
                        +"\ntype:"+message.getType());
                final MyAlertDialog dialog = new MyAlertDialog(ChatMsgActivity.this,
                        new String[]{"复制", "转发", "删除"}
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //复制
                                if (message.getType().equals(SEND_TEXT)|| message.getType()==SEND_TEXT){
                                    if (Build.VERSION.SDK_INT > 11) {
                                        ClipboardManager clipboard = (ClipboardManager) mContext
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("Simple text", message.getText());
                                        clipboard.setPrimaryClip(clip);
                                    } else {
                                        android.text.ClipboardManager clip = (android.text.ClipboardManager) mContext
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        if (clip.hasText()) {
                                            clip.getText();
                                        }
                                    }

                                    showToast(ChatMsgActivity.this,"复制成功");
                                }else {
                                    showToast(ChatMsgActivity.this,"复制类型错误");
                                }
                                break;

                            case 1:

                                break;

                            default:
                                //2\从本地删除
                                conversation.deleteMessage(new Integer(message.getMsgID()));
                                //移除视图
                                mAdapter.deleteById(message.getMsgId());
                                mAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });

                dialog.initDialog();

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
//        mChatInput.setMenuContainerHeight(keyboardHeight);

    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatView.setMinimumHeight(oldh - h);
        }
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
            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
            }
            Log.e("onkeyboardHeight", ":" + keyboardHeight);
            mChatInput.setMenuContainerHeight(keyboardHeight);

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
        Log.e("onShowKeyboard : key = ", "" + keyboardHeight);
//        mChatInput.setMenuContainerHeight(keyboardHeight);


    }

    private void onHideKeyboard() {
        // 在这里处理软键盘收回的回调
        Log.e("onHidekey = ", "dd");
//        mChatInput.setMenuContainerHeight(-keyboardHeight);
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

    @OnClick({R.id.title_bar_back, R.id.title_options_tv, R.id.chat_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                break;
            case R.id.title_options_tv:
                break;
            case R.id.chat_send:
//                MyMessage me;
//                me.setDuration();
//                message.
//                JMessageClient.sendMessage(message);
                final Message message1 = JMessageClient.createSingleTextMessage(userName, "", mChatEt.getText().toString());
                MyMessage myMessage;

                message1.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            mAdapter.addToStart(new MyMessage(mChatEt.getText().toString(), SEND_TEXT), true);
                            mChatEt.setText("");
                        } else {
                            Log.e("sendMsg", s);
                        }
                    }
                });
                JMessageClient.sendMessage(message1);
                if (mData!=null){
                    mData.clear();
                }
                break;
        }
    }
}
