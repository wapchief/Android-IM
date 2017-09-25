package com.wapchief.jpushim.entity;

import android.widget.TextView;

import java.io.File;
import java.io.Serializable;

import cn.jpush.im.android.api.model.Conversation;

/**
 * type:
 * 1 好友推荐,2 好友验证 3 好友列表 0会话列表
 *
 */

public class MessageBean implements Serializable {
    //登录状态
    public boolean login;
    //在线状态
    public boolean online;
    public int type;
    public String img;
    public String msgID;
    public String title;
    public String content;
    public String time;
    public String userName;
    public Boolean isFriends;
    public Conversation conversation;
    public int MsgType;
    private static final long serialVersionUID = 1L;
    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "type=" + type +
                ", img='" + img + '\'' +
                ", msgID='" + msgID + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", userName='" + userName + '\'' +
                ", isFriends=" + isFriends +
                ", conversation=" + conversation +
                ", MsgType=" + MsgType +
                '}';
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
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

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
