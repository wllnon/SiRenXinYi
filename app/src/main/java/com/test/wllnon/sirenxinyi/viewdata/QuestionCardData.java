package com.test.wllnon.sirenxinyi.viewdata;

import android.graphics.drawable.Drawable;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class QuestionCardData extends BaseCardData {
    private User user;
    private Question question;

    public QuestionCardData() {
        setCardKind(Constant.QUESTION);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        QuestionCardData questionCardData = (QuestionCardData) baseCardData;
        setUser(questionCardData.getUser());
        setQuestion(questionCardData.getQuestion());
        this.setCardKind(questionCardData.getCardKind());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
