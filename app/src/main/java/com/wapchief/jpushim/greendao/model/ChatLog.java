package com.wapchief.jpushim.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Wu on 2017/5/8 0008 下午 2:39.
 * 描述：聊天记录
 */
@Entity
public class ChatLog {

    @Id(autoincrement = true)
    private Long id;
    //用户ID
    private Long userId;
    //时间
    private String time;
    //内容
    private String content;




    @Generated(hash = 1431079095)
    public ChatLog(Long id, Long userId, String time, String content) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.content = content;
    }

    @Generated(hash = 1994978153)
    public ChatLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
