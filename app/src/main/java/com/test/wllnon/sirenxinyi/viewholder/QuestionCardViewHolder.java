package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.PersonalInfoActivity;
import com.test.wllnon.sirenxinyi.activity.QuestionDetailActivity;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.QuestionCardData;

import java.sql.Date;

import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Administrator on 2016/4/12.
 */
public class QuestionCardViewHolder extends BaseCardViewHolder implements View.OnClickListener {
    private TextView time;
    private CircleImageView avatar;
    private TextView title;
    private TextView browsers;
    private TextView answers;
    private TagGroup tagGroup;

    private LinearLayout cardView;

    private QuestionCardData questionCardData;

    public QuestionCardViewHolder(Activity activity, View view) {
        super(activity, Constant.QUESTION, view);
        findViews(view);
        setListeners();
    }

    public void findViews(View view) {
        time = (TextView) view.findViewById(R.id.time_question_detail);
        title = (TextView) view.findViewById(R.id.title_question_cardview);
        browsers = (TextView) view.findViewById(R.id.browser_question_cardview);
        answers = (TextView) view.findViewById(R.id.answer_question_cardview);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_question_cardview);
        tagGroup = (TagGroup) view.findViewById(R.id.tag_group_question_cardview);

        cardView = (LinearLayout) view.findViewById(R.id.cardview_question_cardview);
    }

    public void setListeners() {
        cardView.setOnClickListener(this);
        avatar.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        super.setCardData(baseCardData);

        questionCardData = (QuestionCardData) baseCardData;
        setTimeText(FormatUtils.getInstance().dateFormatter(new Date(questionCardData.getQuestion().getTime().getTime())));
        setTitleText(questionCardData.getQuestion().getTitle());
        setAnswers(FormatUtils.getInstance().decimalSimplifyFormatter(questionCardData.getQuestion().getCommentNumber()));
        setBrowsers(FormatUtils.getInstance().decimalSimplifyFormatter(questionCardData.getQuestion().getBrowserNumber()));
        setAvatarWithUrl(questionCardData.getUser().getAvatarUrl());
        setTags(questionCardData.getQuestion().getTags());
        setCardKind(questionCardData.getCardKind());
    }

    public void setTimeText(String timeText) {
        if (time != null) {
            time.setText(timeText);
        }
    }

    public void setTitleText(String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
    }

    public void setBrowsers(String browserNum) {
        if (browsers != null) {
            browsers.setText(browserNum);
        }
    }

    public void setAnswers(String answerNum) {
        if (answers != null) {
            answers.setText(answerNum);
        }
    }

    public void setAvatarWithUrl(String avatarUrl) {
        if (avatar != null) {
            Glide.with(activity)
                    .load(avatarUrl)
                    .error(R.drawable.ic_account_circle_grey_600_48dp)
                    .crossFade(1200)
                    .into(avatar);
        }
    }

    public void setTags(String[] tagList) {
        if (tagGroup != null) {
            tagGroup.setTags(tagList);
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("FUCK", "onClick: ");
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cardview_question_cardview:
                Log.i("FUCK", "onClick: 0");
                intent = new Intent(getActivity(), QuestionDetailActivity.class);
                intent.putExtra("data", GsonUtils.newInstance().getGson().toJson(questionCardData));
                break;
            case R.id.avatar_question_cardview:
                Log.i("FUCK", "onClick: 1");
                intent = new Intent(activity, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(questionCardData.getUser()));
                break;
        }
        activity.startActivity(intent);
    }
}
