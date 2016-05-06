package com.test.wllnon.sirenxinyi.pojo;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Comment {
    private long id;
    private String content;
    private Time time;
    private int praiseNumber;

    public Comment(int praiseNumber, long id, String content, Time time) {
        this.praiseNumber = praiseNumber;
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }
}
