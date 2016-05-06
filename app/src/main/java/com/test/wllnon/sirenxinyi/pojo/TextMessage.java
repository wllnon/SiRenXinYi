package com.test.wllnon.sirenxinyi.pojo;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.sql.Time;

/**
 * Created by Administrator on 2016/4/21.
 */
public class TextMessage extends Message {

    public TextMessage(int id, String content, Time time) {
        super(id, MessageType.TEXT_MESSAGE, content, time);
    }

    @Override
    public IMMessage getIMMessage() {
        return MessageBuilder.createTextMessage("1301671535@qq.com", SessionTypeEnum.P2P, getContent());
    }

}
