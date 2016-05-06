package com.test.wllnon.sirenxinyi.viewdata;

/**
 * Created by Administrator on 2016/2/21.
 */
public abstract class BaseCardData {
    private int cardKind;

    private boolean isVisibility = true;

    public boolean isVisibility() {
        return isVisibility;
    }

    public void setIsVisibility(boolean isVisibility) {
        this.isVisibility = isVisibility;
    }

    public abstract void setCardData(BaseCardData baseCardData);

    public int getCardKind() {
        return cardKind;
    }

    public void setCardKind(int cardKind) {
        this.cardKind = cardKind;
    }

}
