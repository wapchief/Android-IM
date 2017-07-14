package com.wapchief.jpushim.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.framework.base.BaseAcivity;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscodeListener;

/**
 * Created by wapchief on 2017/5/8 0008 下午 3:39.
 * 描述：注册
 */
public class LoginActivity extends BaseAcivity {

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
    private int time = 60;
    SharedPrefHelper sharedPrefHelper;
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
        if (!sharedPrefHelper.getUserId().equals("")) {
            loginPassWord.setText(sharedPrefHelper.getUserPW());
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.login_code_bt, R.id.login_submit, R.id.login_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_code_bt:
                showToast(this, "112221");
                startCount();
                SMSSDK.getInstance().getSmsCodeAsyn(loginUsername.getText().toString(), "1", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(String s) {
                        loginCode.setText(s);
                    }

                    @Override
                    public void getCodeFail(int i, String s) {
                        showToast(LoginActivity.this, s);
                    }
                });
                break;
            case R.id.login_submit:
                //注册
                JMessageClient.register(loginUsername.getText().toString(), loginPassWord.getText().toString(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.e("s=======1:",i+"，"+ s);
                        switch (i){
                            case 898001:
                                showToast(LoginActivity.this,"用户名已存在");
                                break;
                            case 871301:
                                showToast(LoginActivity.this,"密码格式错误");
                                break;
                            case 871304:
                                showToast(LoginActivity.this,"密码错误");
                                break;
                        }
                    }
                });
                break;
            case R.id.login_ok:
                //登陆
                JMessageClient.login(loginUsername.getText().toString(), loginPassWord.getText().toString(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.e("s=======2:",i+"，"+ s);
                        switch (i){
                            case 801003:
                                showToast(LoginActivity.this,"用户名不存在");
                                break;
                            case 871301:
                                showToast(LoginActivity.this,"密码格式错误");
                                break;
                            case 801004:
                                showToast(LoginActivity.this,"密码错误");
                                break;
                            default:
                                showToast(LoginActivity.this,"登陆成功");
                                sharedPrefHelper.setUserId(loginUsername.getText().toString());
                                sharedPrefHelper.setUserPW(loginPassWord.getText().toString());
                                break;
                        }

                    }
                });
                break;
        }
    }

    // 验证码按钮
    public void startCount() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                time--;
                if (time <= 0) {
                    mHandler.sendEmptyMessage(5);
                } else {
                    mHandler.sendEmptyMessage(4);
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        new Thread(runnable).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // hidenSoft(mUserPwd);
                    // if (tomain) {
                    // Intent i = new Intent(LoginActivity.this,
                    // MainActivity.class);
                    // LoginActivity.this.startActivity(i);
                    // overridePendingTransition(R.anim.anim_enter,
                    // R.anim.anim_exit);
                    // }
                    setResult(RESULT_OK);
                    LoginActivity.this.finish();
                    break;
                case 4:
                    loginCodeBt.setEnabled(false);
                    loginCodeBt.setTextColor(Color.rgb(32, 32, 32));
                    loginCodeBt.setText("已发送(" + String.valueOf(time) + ")");
                    break;
                case 5:
                    loginCodeBt.setText("重新获取验证码");
                    loginCodeBt.setEnabled(true);
                    time = 60;
                    break;
            }

        }
    };


}
