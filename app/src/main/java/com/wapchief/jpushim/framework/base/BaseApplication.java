package com.wapchief.jpushim.framework.base;

import android.app.Application;
import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.wapchief.jpushim.greendao.DaoMaster;
import com.wapchief.jpushim.framework.helper.MySQLiteOpenHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.sms.SMSSDK;

/**
 * Created by wapchief on 2017/4/13 0013 上午 11:23.
 * 描述：自定义Application
 */
public class BaseApplication extends Application {


    public static BaseApplication baseApplication;
    private Context mContext;
    public MySQLiteOpenHelper helper;
    private DaoMaster master;

    @Override
    public void onCreate() {
        super.onCreate();
        //开启极光调试
        JPushInterface.setDebugMode(true);
        baseApplication = this;
        mContext = BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(mContext, true);
        //初始化极光sms
        SMSSDK.getInstance().initSdk(mContext);
        //初始化数据库
        setupDatabase();

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
