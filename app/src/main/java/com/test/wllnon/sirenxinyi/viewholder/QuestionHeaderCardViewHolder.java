package com.test.wllnon.sirenxinyi.viewholder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.customview.InterceptScrollView;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.utils.QuickReturnUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.QuestionCardData;

import java.sql.Date;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Administrator on 2016/4/25.
 */
public class QuestionHeaderCardViewHolder extends BaseCardViewHolder
        implements View.OnClickListener, Animator.AnimatorListener {
    private TextView time;
    private CircleImageView avatar;
    private TextView title;
    private TextView browsers;
    private TextView answers;
    private TagGroup tagGroup;

    private CardView collapse;
    private TextView fakeContent;
    private TextView content;
    private InterceptScrollView scrollView;

    private LinearLayout cardView;

    private QuestionCardData questionCardData;

    private boolean isCollapsed = true;
    private final int startHeight;
    private final int endHeight;


    public QuestionHeaderCardViewHolder(Activity activity, View view) {
        super(activity, Constant.HEADER, view);
        findViews(view);
        setListeners();

        startHeight = QuickReturnUtils.dp2px(activity, 96);
        endHeight = QuickReturnUtils.dp2px(activity, 280);
    }

    public void findViews(View view) {
        time = (TextView) view.findViewById(R.id.time_question_header);
        title = (TextView) view.findViewById(R.id.title_question_header);
        browsers = (TextView) view.findViewById(R.id.browser_question_header);
        answers = (TextView) view.findViewById(R.id.answer_question_header);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_question_header);
        tagGroup = (TagGroup) view.findViewById(R.id.tag_group_question_header);

        collapse = (CardView) view.findViewById(R.id.collapse_question_header);
        content = (TextView) view.findViewById(R.id.content_question_header);
        fakeContent = (TextView) view.findViewById(R.id.fake_content_question_header);
        scrollView = (InterceptScrollView) view.findViewById(R.id.scrollView_question_header);

        cardView = (LinearLayout) view.findViewById(R.id.cardview_question_header);
    }

    public void setListeners() {
        cardView.setOnClickListener(this);
        avatar.setOnClickListener(this);
        collapse.setOnClickListener(this);
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
        setContentText(questionCardData.getQuestion().getContent());
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

    public void setContentText(String text) {
        fakeContent.setText(text);
        content.setText(text);
        if (isCollapsed) {
            fakeContent.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            fakeContent.setHeight(startHeight);
        } else {
            fakeContent.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO: 2016/4/25
        switch (v.getId()) {
            case R.id.collapse_question_header:
                ObjectAnimator animator;
                animator = ObjectAnimator
                        .ofInt(fakeContent,
                                "height",
                                isCollapsed ? startHeight : endHeight,
                                isCollapsed ? endHeight : startHeight)
                        .setDuration(800);
                animator.addListener(this);
                animator.start();
                ObjectAnimator.ofFloat(collapse,
                                "rotation",
                                isCollapsed ? 0 : 180,
                                isCollapsed ? 180 : 0)
                        .setDuration(800)
                        .start();
                break;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (!isCollapsed) {
            scrollView.setVisibility(View.GONE);
            fakeContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isCollapsed) {
            fakeContent.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
        isCollapsed = !isCollapsed;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}
