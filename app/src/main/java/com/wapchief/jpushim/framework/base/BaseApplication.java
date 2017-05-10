package com.wapchief.jpushim.framework.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wapchief.jpushim.greendao.DaoMaster;
import com.wapchief.jpushim.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Wu on 2017/4/13 0013 上午 11:23.
 * 描述：自定义Application
 */
public class BaseApplication extends Application{


    private Context mContext;
    public DaoSession daoSession;
    public SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;
    @Override
    public void onCreate() {
        super.onCreate();
        //开启极光调试
        JPushInterface.setDebugMode(true);

        mContext=BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(mContext,true);
        //初始化数据库
        setupDatabase();

    }
    private void setupDatabase() {
        //创建数据库
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(this, "test", null);
        //得到数据库连接对象
        db = helper.getWritableDatabase();
        //得到数据库管理者
        daoMaster =new DaoMaster(db);
        //得到daoSession，可以执行增删改查操作
        daoSession = daoMaster.newSession();

        //调试
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

    }
}
