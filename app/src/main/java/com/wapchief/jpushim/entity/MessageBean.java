package com.wapchief.jpushim.entity;

import android.widget.TextView;

/**
 * Created by apple on 2017/6/23.
 */

public class MessageBean {
    public int img;

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

    public String title;
    public String content;
    public String time;

    @Override
    public String toString() {
        return "MainBean{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
