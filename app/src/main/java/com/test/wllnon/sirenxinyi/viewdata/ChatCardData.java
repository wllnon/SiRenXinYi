package com.test.wllnon.sirenxinyi.viewdata;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.Message;
import com.test.wllnon.sirenxinyi.pojo.User;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ChatCardData extends BaseCardData {
    private User user;          // 交流对象
    private Message message;
    private boolean isSent;

    public ChatCardData() {
        setCardKind(Constant.CHAT);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        ChatCardData chatCardData = (ChatCardData) baseCardData;
        this.setUser(chatCardData.getUser());
        this.setMessage(chatCardData.getMessage());
        this.setSent(chatCardData.isSent());
        this.setCardKind(chatCardData.getCardKind());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setSent(boolean isSend) {
        this.isSent = isSend;
    }

    public boolean isSent() {
        return this.isSent;
    }

}
