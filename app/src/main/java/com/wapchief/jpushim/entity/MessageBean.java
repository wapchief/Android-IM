package com.wapchief.jpushim.entity;

import android.widget.TextView;

/**
 * Created by apple on 2017/6/23.
 * type:
 * 1 好友推荐
 */

public class MessageBean {
    public int type;
    public int img;
    public String title;
    public String content;
    public String time;
    public String userName;
    public Boolean isFriends;

    @Override
    public String toString() {
        return "MessageBean{" +
                "type=" + type +
                ", img=" + img +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", userName='" + userName + '\'' +
                ", isFriends=" + isFriends +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getFriends() {
        return isFriends;
    }

    public void setFriends(Boolean friends) {
        isFriends = friends;
    }
}
