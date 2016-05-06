package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;

/**
 * Created by Administrator on 2016/2/21.
 */
public class BaseCardViewHolder extends RecyclerView.ViewHolder {
    protected int cardKind;
    protected Activity activity;

    public BaseCardViewHolder(Activity activity, int cardKind, View view) {
        super(view);
        setCardKind(cardKind);
        this.activity = activity;
    }

    public int getCardKind() {
        return cardKind;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setCardKind(int cardKind) {
        this.cardKind = cardKind;
    }

    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        if (baseCardData.isVisibility())
            this.itemView.setVisibility(View.VISIBLE);
        else
            this.itemView.setVisibility(View.INVISIBLE);
    }
}
