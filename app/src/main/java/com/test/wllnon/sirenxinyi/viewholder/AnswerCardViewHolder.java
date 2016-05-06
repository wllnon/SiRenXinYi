package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.AnswerDescriptionActivity;
import com.test.wllnon.sirenxinyi.activity.PersonalInfoActivity;
import com.test.wllnon.sirenxinyi.activity.QuestionDetailActivity;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.pojo.Answer;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.AnswerCardData;

/**
 * Created by Administrator on 2016/2/21.
 */
public class AnswerCardViewHolder extends BaseCardViewHolder implements View.OnClickListener {
    private TextView tag;
    private TextView title;
    private TextView content;
    private TextView praiseNumber;
    private CircleImageView avatar;

    private LinearLayout cardView;

    private AnswerCardData answerCardData;

    public AnswerCardViewHolder(Activity activity, View view) {
        super(activity, Constant.ANSWER, view);
        findViews(view);
        setListeners();
    }

    private void findViews(View view) {
        tag = (TextView) view.findViewById(R.id.tag_normal_cardview);
        title = (TextView) view.findViewById(R.id.title_normal_cardview);
        content = (TextView) view.findViewById(R.id.content_normal_cardview);
        praiseNumber = (TextView) view.findViewById(R.id.praise_normal_cardview);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_normal_cardview);

        cardView = (LinearLayout) view.findViewById(R.id.cardview_normal_cardview);
    }

    private void setListeners() {
        cardView.setOnClickListener(this);
        title.setOnClickListener(this);
        avatar.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        if (baseCardData == null) {
            return;
        }

        answerCardData = (AnswerCardData) baseCardData;
        Answer answer = answerCardData.getAnswer();
        Question question = answerCardData.getQuestion();
        User user = answerCardData.getUser();

        setTagText(answer.getTag());
        setContentText(answer.getSimpleContent());
        setTitleText(question.getTitle());
        setAvatarWithUrl(user.getAvatarUrl());

        setPraiseNumberText(FormatUtils.getInstance().
                decimalSimplifyFormatter(answer.getPraiseNumber()));

        setCardKind(answerCardData.getCardKind());
    }

    public void setTagText(String tagText) {
        if (tag != null) {
            tag.setText(tagText);
        }
    }

    public void setTitleText(String titleText) {
        if (titleText != null) {
            title.setText(titleText);
        }
    }

    public void setContentText(String contentText) {
        if (content != null) {
            content.setText(contentText);
        }
    }

    public void setPraiseNumberText(String praiseNumberText) {
        if (praiseNumber != null) {
            praiseNumber.setText(praiseNumberText);
        }
    }

    public void setAvatarWithUrl(String avatarUrl) {
        if (avatar != null) {
            Glide.with(activity)
                    .load(avatarUrl)
                    .error(R.drawable.ic_account_circle_grey_600_48dp)
                    .crossFade(1500)
                    .into(avatar);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cardview_normal_cardview:
                intent = new Intent(getActivity(), AnswerDescriptionActivity.class);
                intent.putExtra("data", GsonUtils.newInstance().getGson().toJson(answerCardData));
                break;
            case R.id.title_normal_cardview:
                intent = new Intent(activity, QuestionDetailActivity.class);
                intent.putExtra("question", GsonUtils.newInstance().getGson().toJson(answerCardData.getQuestion()));
                break;
            case R.id.avatar_normal_cardview:
                intent = new Intent(activity, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(answerCardData.getUser()));
                break;
        }
        activity.startActivity(intent);
    }
}
