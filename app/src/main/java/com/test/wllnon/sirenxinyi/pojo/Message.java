package com.test.wllnon.sirenxinyi.pojo;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.sql.Time;

/**
 * Created by Administrator on 2016/5/4.
 */
public abstract class Message {
    private long id;
    private String content;
    private Time time;
    private final MessageType type;

    public enum MessageType {
        TEXT_MESSAGE,
        AUDIO_MESSAGE,
        PICTURE_MESSAGE
    }

    public Message(long id, String content, Time time) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.type = MessageType.TEXT_MESSAGE;
    }

    protected Message(long id, MessageType type, String content, Time time) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.type = type;
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

    public MessageType getType() {
        return type;
    }

    public abstract IMMessage getIMMessage();

}
