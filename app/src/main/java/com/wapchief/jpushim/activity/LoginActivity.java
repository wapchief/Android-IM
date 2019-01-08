package com.wapchief.jpushim.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.MainActivity;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseActivity;
import com.wapchief.jpushim.framework.base.BaseApplication;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.utils.BitMapUtils;
import com.wapchief.jpushim.framework.utils.StringUtils;
import com.wapchief.jpushim.framework.utils.TimeUtils;
import com.wapchief.jpushim.greendao.model.User;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.LoginEvent;
import cn.jiguang.analytics.android.api.RegisterEvent;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/5/8 0008 下午 3:39.
 * 描述：注册登陆
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_passWord)
    EditText loginPassWord;
    @BindView(R.id.login_code)
    EditText loginCode;
    @BindView(R.id.login_code_bt)
    Button loginCodeBt;
    @BindView(R.id.login_submit)
    Button loginSubmit;
    @BindView(R.id.login_ok)
    Button loginOk;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    private int time = 60;
    SharedPrefHelper sharedPrefHelper;
    private UserInfo userInfo;

    Handler handler=new Handler();
    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.getUserId().equals("")) {
            loginUsername.setText(sharedPrefHelper.getUserId());
        }
//        if (!sharedPrefHelper.getUserId().equals("")) {
//            loginPassWord.setText(sharedPrefHelper.getUserPW());
//        }
        mTitleBarBack.setVisibility(View.GONE);
        mTitleOptionsImg.setVisibility(View.GONE);
        mTitleBarTitle.setText("注册登陆");
    }

    @Override
    protected void initData() {

    }



    @OnClick({R.id.login_code_bt, R.id.login_submit, R.id.login_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_code_bt:
//                startCount();
//                SMSSDK.getInstance().getSmsCodeAsyn(loginUsername.getText().toString(), "1", new SmscodeListener() {
//                    @Override
//                    public void getCodeSuccess(String s) {
//                        loginCode.setText(s);
//                    }
//
//                    @Override
//                    public void getCodeFail(int i, String s) {
//                        showToast(LoginActivity.this, s);
//                    }
//                });
                break;
            case R.id.login_submit:
                //注册
                JMessageClient.register(loginUsername.getText().toString(), loginPassWord.getText().toString(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.e("s=======1:", i + "，" + s);
                        switch (i) {
                            case 0:
                                showToast(LoginActivity.this, "注册成功");
                                initLogin(loginUsername.getText().toString(),loginPassWord.getText().toString(),1);
                                RegisterEvent event = new RegisterEvent("userName", true);
                                JAnalyticsInterface.onEvent(getContext(),event);
                                break;
                            case 898001:
                                showToast(LoginActivity.this, "用户名已存在");
                                break;
                            case 871301:
                                showToast(LoginActivity.this, "密码格式错误");
                                break;
                            case 871304:
                                showToast(LoginActivity.this, "密码错误");
                                break;
                            default:
                                showToast(LoginActivity.this, s);
                                break;
                        }
                    }
                });
                break;
            case R.id.login_ok:
                //登陆
//                Log.e("info2============",""+JMessageClient.getMyInfo());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initLogin(loginUsername.getText().toString(),loginPassWord.getText().toString(),0);
                    }
                }, 500);
                break;
                default:break;

        }
    }

    /**
     *
     * @param userName
     * @param passWord
     */
    private void initLogin(String userName, String passWord, final int type){
        showProgressDialog("正在登陆...");
        JMessageClient.login(userName, passWord, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                dismissProgressDialog();
                switch (i) {
                    case 801003:
                        showToast(LoginActivity.this, "用户名不存在");
                        break;
                    case 871301:
                        showToast(LoginActivity.this, "密码格式错误");
                        break;
                    case 801004:
                        showToast(LoginActivity.this, "密码错误");
                        handler.sendEmptyMessage(-1);
                        break;
                    case 0:
                        showToast(LoginActivity.this, "登陆成功");
                        sharedPrefHelper.setUserId(loginUsername.getText().toString());
                        sharedPrefHelper.setUserPW(loginPassWord.getText().toString());
                        initUserInfo(loginUsername.getText().toString(),type);
                        //登录成功计数
                        LoginEvent event = new LoginEvent("userName", true);
                        JAnalyticsInterface.onEvent(getContext(),event);
                        break;
                        default:

                            break;
                }

            }
        });
    }
    //初始化个人资料
    public void initUserInfo(String id, final int type){
      showProgressDialog("正在初始化数据");
        JMessageClient.getUserInfo(id, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                dismissProgressDialog();
                if (i==0) {
//                    Log.e("info-Login", ""+JMessageClient.getMyInfo()+"\n"+JMessageClient.getConversationList()+"\n"+userInfo);
                    Intent intent = new Intent(LoginActivity.this
                            , MainActivity.class);
                    intent.putExtra("LOGINTYPE", type);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            dialog();
//        }
//        return false;
//    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

//    private static Boolean isExit = false;
//    /*单击回退*/
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitBy2Click();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /*双击退出*/
//    private void exitBy2Click() {
//        Timer tExit = null;
//        if (isExit == false) {
//            isExit = true; // 准备退出
//            showLongToast(this, "再按一次退出程序");
//            tExit = new Timer();
//            tExit.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false; // 取消退出
//                }
//            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
//
//        } else {
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
//
//            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            manager.killBackgroundProcesses(getPackageName());
//        }
//    }
}
