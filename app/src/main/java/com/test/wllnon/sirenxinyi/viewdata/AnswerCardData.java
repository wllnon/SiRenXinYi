package com.test.wllnon.sirenxinyi.viewdata;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.Answer;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.pojo.User;

/**
 * Created by Administrator on 2016/2/21.
 */
public class AnswerCardData extends BaseCardData {
    private User user;
    private Answer answer;
    private Question question;
    private boolean isPraised;

    public AnswerCardData() {
        setCardKind(Constant.ANSWER);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        AnswerCardData answerCardData = (AnswerCardData)baseCardData;
        this.setUser(answerCardData.getUser());
        this.setAnswer(answerCardData.getAnswer());
        this.setQuestion(answerCardData.getQuestion());
        this.setIsPraised(answerCardData.isPraised());
        this.setCardKind(answerCardData.getCardKind());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setIsPraised(boolean isPraised) {
        this.isPraised = isPraised;
    }
}
