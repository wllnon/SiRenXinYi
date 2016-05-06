package com.test.wllnon.sirenxinyi.viewdata;

import com.test.wllnon.sirenxinyi.constant.Constant;

/**
 * Created by Administrator on 2016/2/22.
 */
public class ClassifyCardData extends BaseCardData {
    private String tag;
    private String title;
    private int color;

    public ClassifyCardData() {
        setCardKind(Constant.CLASSIFY);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        ClassifyCardData classifyCardData = (ClassifyCardData) baseCardData;
        this.setTag(classifyCardData.getTag());
        this.setTitle(classifyCardData.getTitle());
        this.setColor(classifyCardData.getColor());
        this.setCardKind(classifyCardData.getCardKind());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
