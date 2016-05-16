package com.wteam.carkeeper.entity;

import java.util.Date;

/**
 * Created by lhb on 2016/5/6.
 */
public class SystemInfo {

    private String title;
    private String content;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

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


}
