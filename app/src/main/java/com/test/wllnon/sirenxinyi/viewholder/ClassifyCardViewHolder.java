package com.test.wllnon.sirenxinyi.viewholder;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.ClassifyCardData;


/**
 * Created by Administrator on 2016/2/21.
 */
public class ClassifyCardViewHolder extends BaseCardViewHolder {
    private TextView tag;
    private TextView title;
    private CardView cardView;

    public ClassifyCardViewHolder(Activity activity, View view) {
        super(activity, Constant.CLASSIFY, view);
        findView(view);
    }

    public void findView(View view) {
        tag = (TextView) view.findViewById(R.id.tag_classify_cardview);
        title = (TextView) view.findViewById(R.id.title_classify_cardview);
        cardView = (CardView) view.findViewById(R.id.classify_cardview);

        view.setLayoutParams(
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, 360 + (int)(Math.random()*360)));
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        ClassifyCardData classifyCardData = (ClassifyCardData) baseCardData;
        if (classifyCardData == null) {
            return;
        }
        setTagText(classifyCardData.getTag());
        setTitleText(classifyCardData.getTitle());
        setCardViewBackgroundColor(classifyCardData.getColor());
        setCardKind(classifyCardData.getCardKind());
    }

    public void setTagText(String tagText) {
        if (tag != null) {
            tag.setText(tagText);
        }
    }

    public void setTitleText(String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
    }

    public void setCardViewBackgroundColor(int color) {
        if (cardView != null) {
            cardView.setCardBackgroundColor(color);
        }
    }
}
