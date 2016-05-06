package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;

/**
 * Created by Administrator on 2016/4/20.
 */
public class FooterCardViewHolder extends BaseCardViewHolder {
    private ProgressBar progressBar;
    private TextView content;

    public FooterCardViewHolder(Activity activity, View view) {
        super(activity, Constant.FOOTER, view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_cardview_footer);
        content = (TextView) view.findViewById(R.id.content_cardview_footer);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {}

    public void setRefreshing(boolean isRefreshing) {
        progressBar.setVisibility(isRefreshing ? View.VISIBLE : View.GONE);
        content.setVisibility(isRefreshing ? View.GONE : View.VISIBLE);
    }

    public void setText(String text) {
        content.setText(text);
    }

}
