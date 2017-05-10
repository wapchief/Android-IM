package com.wapchief.jpushim.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Wu on 2017/5/8 0008 下午 2:32.
 * 描述：基础类
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 1248599927)
    public User(Long id) {
        this.id = id;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
