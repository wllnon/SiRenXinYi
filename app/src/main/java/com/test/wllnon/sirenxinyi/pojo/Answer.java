package com.test.wllnon.sirenxinyi.pojo;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Answer {
    private long id;
    private String tag;
    private String simpleContent;
    private Time time;
    private int praiseNumber;

    public Answer(long id, String tag, String simpleContent, Time time, int praiseNumber) {
        this.id = id;
        this.tag = tag;
        this.simpleContent = simpleContent;
        this.time = time;
        this.praiseNumber = praiseNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSimpleContent() {
        return simpleContent;
    }

    public void setSimpleContent(String simpleContent) {
        this.simpleContent = simpleContent;
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
