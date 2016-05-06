package com.test.wllnon.sirenxinyi.viewdata;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.TextMessage;
import com.test.wllnon.sirenxinyi.pojo.User;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MessageCardData extends BaseCardData {
    private User user;          // 交流对象
    private TextMessage textMessage;

    public MessageCardData() {
        setCardKind(Constant.MESSAGE);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        MessageCardData messageCardData = (MessageCardData) baseCardData;
        this.setUser(messageCardData.getUser());
        this.setTextMessage(messageCardData.getTextMessage());
        // Pay attention here, you should set the kind of card
        this.setCardKind(messageCardData.getCardKind());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }



}
