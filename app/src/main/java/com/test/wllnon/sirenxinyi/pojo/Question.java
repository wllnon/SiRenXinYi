package com.test.wllnon.sirenxinyi.pojo;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Question {
    private long id;
    private String[] tags;
    private String title;
    private String content;
    private int commentNumber;
    private int browserNumber;
    private Time time;

    public Question(long id, String[] tags, String title, String content, int commentNumber, int browserNumber, Time time) {
        this.id = id;
        this.tags = tags;
        this.title = title;
        this.content = content;
        this.commentNumber = commentNumber;
        this.browserNumber = browserNumber;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getBrowserNumber() {
        return browserNumber;
    }

    public void setBrowserNumber(int browserNumber) {
        this.browserNumber = browserNumber;
    }
}
