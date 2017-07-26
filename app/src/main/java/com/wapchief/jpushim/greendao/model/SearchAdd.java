package com.wapchief.jpushim.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wapchief on 2017/7/25.
 * 搜索好友历史
 */

@Entity
public class SearchAdd {
    @Id(autoincrement = true)
    private Long id;
    private String content;
    @Generated(hash = 129942465)
    public SearchAdd(Long id, String content) {
        this.id = id;
        this.content = content;
    }
    @Generated(hash = 1979377860)
    public SearchAdd() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
