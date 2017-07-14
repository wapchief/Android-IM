package com.wapchief.jpushim.framework.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wapchief.jpushim.greendao.DaoMaster;
import com.wapchief.jpushim.greendao.DaoSession;

/**
 * Created by wapchief on 2017/7/14.
 * 数据库初始化的辅助类
 */

public class GreenDaoHelper {

    Context context;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;


    public GreenDaoHelper(Context context) {
        this.context = context;
    }

    public DaoSession initDao(){
        helper = new DaoMaster.DevOpenHelper(context, "test", null);
        db= helper.getWritableDatabase();
        daoMaster= new DaoMaster(db);
        daoSession= daoMaster.newSession();
        return daoSession;
    }
}
