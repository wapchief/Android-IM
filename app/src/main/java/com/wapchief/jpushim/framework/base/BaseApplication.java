package com.wapchief.jpushim.framework.base;

import android.app.Application;
import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.greendao.DaoMaster;
import com.wapchief.jpushim.framework.helper.MySQLiteOpenHelper;
import com.wapchief.jpushim.view.MyCompoundButton;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.sms.SMSSDK;

import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_DEFAULT;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_SILENCE;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_SOUND;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_VIBRATE;
import static cn.jpush.im.android.api.JMessageClient.NOTI_MODE_DEFAULT;
import static cn.jpush.im.android.api.JMessageClient.NOTI_MODE_NO_SOUND;
import static cn.jpush.im.android.api.JMessageClient.NOTI_MODE_NO_VIBRATE;
import static cn.jpush.im.android.api.JMessageClient.NOTI_MODE_SILENCE;

/**
 * Created by wapchief on 2017/4/13 0013 上午 11:23.
 * 描述：自定义Application
 */
public class BaseApplication extends Application {


    public static BaseApplication baseApplication;
    private Context mContext;
    public MySQLiteOpenHelper helper;
    private DaoMaster master;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        sharedPrefHelper = SharedPrefHelper.getInstance();
        sharedPrefHelper.setRoaming(true);
        //开启极光调试
        JPushInterface.setDebugMode(true);
        mContext = BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(mContext, true);

        //初始化极光sms
        SMSSDK.getInstance().initSdk(mContext);
        //初始化数据库
        setupDatabase();
        //通知管理,通知栏开启，其他关闭
        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE);
        //推送状态
        initJPush2();

    }

    private void initJPush2() {
//        switch (JMessageClient.getNotificationFlag()) {
//            case FLAG_NOTIFY_DEFAULT://全部开启
//                //显示通知，有声音，有震动
//                sharedPrefHelper.setMusic(true);
//                sharedPrefHelper.setVib(true);
//                break;
//            case FLAG_NOTIFY_WITH_VIBRATE://2，开启震动
//                //显示通知，无声音，有震动
//                sharedPrefHelper.setMusic(false);
//                break;
//            case FLAG_NOTIFY_WITH_SOUND://1,开启声音
//                //显示通知，有声音，无震动
//                sharedPrefHelper.setMusic(true);
//                break;
//            case FLAG_NOTIFY_SILENCE://0，通知栏开启，其余关闭
                //显示通知，无声音，无震动
                sharedPrefHelper.setMusic(false);
                sharedPrefHelper.setVib(false);

//                break;
//        }
    }

    private void initJPush() {
        switch (JMessageClient.getNotificationFlag()) {
            case NOTI_MODE_DEFAULT:
                //显示通知，有声音，有震动
                sharedPrefHelper.setMusic(true);
                sharedPrefHelper.setVib(true);
                break;
            case NOTI_MODE_NO_SOUND:
                //显示通知，无声音，有震动
                sharedPrefHelper.setMusic(false);
                sharedPrefHelper.setVib(true);
                break;
            case NOTI_MODE_NO_VIBRATE:
                //显示通知，有声音，无震动
                sharedPrefHelper.setMusic(true);
                sharedPrefHelper.setVib(false);
                  break;
            case NOTI_MODE_SILENCE:
                //显示通知，无声音，无震动
                sharedPrefHelper.setMusic(false);
                sharedPrefHelper.setVib(false);
                break;
        }
    }

    private void setupDatabase() {
        //是否开启调试
        MigrationHelper.DEBUG = true;
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        //数据库升级
        helper = new MySQLiteOpenHelper(mContext, "text");
        master = new DaoMaster(helper.getWritableDb());

    }
}
