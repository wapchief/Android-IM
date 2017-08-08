package com.wapchief.jpushim.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wapchief on 2017/7/27.
 * 接收好友请求的类
 */

@Entity
public class RequestList {
    @Id(autoincrement = true)
    private Long id;
    private String msg;
    private String userName;
    private String nakeName;
    private String time;
    private String img;
    @Generated(hash = 2062835296)
    public RequestList(Long id, String msg, String userName, String nakeName,
            String time, String img) {
        this.id = id;
        this.msg = msg;
        this.userName = userName;
        this.nakeName = nakeName;
        this.time = time;
        this.img = img;
    }
    @Generated(hash = 1410998970)
    public RequestList() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNakeName() {
        return this.nakeName;
    }
    public void setNakeName(String nakeName) {
        this.nakeName = nakeName;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
  

}
