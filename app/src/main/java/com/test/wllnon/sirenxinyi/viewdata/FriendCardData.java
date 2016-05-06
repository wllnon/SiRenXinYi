package com.test.wllnon.sirenxinyi.viewdata;

import android.graphics.drawable.Drawable;

import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.User;

/**
 * Created by Administrator on 2016/2/22.
 */
public class FriendCardData extends BaseCardData {
    private User user;

    public FriendCardData() {
        setCardKind(Constant.FRIEND);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        FriendCardData friendCardData = (FriendCardData) baseCardData;
        this.setUser(friendCardData.getUser());
        this.setCardKind(friendCardData.getCardKind());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
