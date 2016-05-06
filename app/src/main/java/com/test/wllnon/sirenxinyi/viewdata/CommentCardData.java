package com.test.wllnon.sirenxinyi.viewdata;

import android.graphics.drawable.Drawable;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.Comment;
import com.test.wllnon.sirenxinyi.pojo.User;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CommentCardData extends BaseCardData {
    private User user;
    private Comment comment;
    private boolean isPraised;

    public CommentCardData() {
        setCardKind(Constant.COMMENT);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if(baseCardData == null) {
            return;
        }
        CommentCardData commentCardData = (CommentCardData) baseCardData;
        this.setUser(commentCardData.getUser());
        this.setComment(commentCardData.getComment());
        this.setCardKind(commentCardData.getCardKind());
        this.setIsPraised(commentCardData.isPraised());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setIsPraised(boolean isPraised) {
        this.isPraised = isPraised;
    }
}
