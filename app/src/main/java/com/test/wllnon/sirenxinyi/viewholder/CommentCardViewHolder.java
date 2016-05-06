package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.pojo.Comment;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.CommentCardData;

import java.sql.Date;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CommentCardViewHolder extends BaseCardViewHolder implements View.OnClickListener {
    private TextView name;
    private CircleImageView avatar;
    private TextView content;
    private TextView time;
    private TextView praise;
    private LikeButton praiseButton;

    private CommentCardData commentCardData;

    public CommentCardViewHolder(Activity activity, View view) {
        super(activity, Constant.COMMENT, view);
        findViews(view);
        setListeners();
    }

    public void findViews(View view) {
        name = (TextView) view.findViewById(R.id.name_comment_item);
        time = (TextView) view.findViewById(R.id.time_comment_item);
        content = (TextView) view.findViewById(R.id.content_comment_item);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_comment_item);
        praise = (TextView) view.findViewById(R.id.praise_comment_item);
        praiseButton = (LikeButton) view.findViewById(R.id.praise_button_comment_item);
    }

    public void setListeners() {
        praiseButton.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        if (baseCardData == null) {
            return;
        }
        commentCardData = (CommentCardData) baseCardData;
        User user = commentCardData.getUser();
        Comment comment = commentCardData.getComment();
        setNameText(user.getName());
        setTimeText(new Date(comment.getTime().getTime()).toString() + " " + comment.getTime().toString());
        setContentText(comment.getContent());
        setPraiseButton(commentCardData.isPraised());
        setPraiseText(FormatUtils.getInstance().decimalSimplifyFormatter(comment.getPraiseNumber()));
        setAvatarWithUrl(user.getAvatarUrl());

        setCardKind(commentCardData.getCardKind());
    }

    public void setNameText(String nameText) {
        if (name != null) {
            name.setText(nameText);
        }
    }

    public void setTimeText(String timeText) {
        if (time != null) {
            time.setText(timeText);
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

    public void setContentText(String contentText) {
        if (content != null) {
            content.setText(contentText);
        }
    }

    public void setPraiseText(String praiseText) {
        if (praise != null) {
            praise.setText(praiseText);
            if (commentCardData.isPraised()) {
                praise.setTextColor(activity.getResources().getColor(R.color.colorAccent));
            } else {
                praise.setTextColor(activity.getResources().getColor(R.color.colorGrayDark));
            }
        }
    }

    public void setPraiseButton(boolean isPraised) {
        if (praiseButton != null) {
            praiseButton.setLiked(isPraised);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.praise_button_comment_item:
                // TODO: 2016/4/22 networks should be done
                if (commentCardData.isPraised()) {
                    commentCardData.setIsPraised(false);
                    commentCardData.getComment().setPraiseNumber(commentCardData.getComment().getPraiseNumber() - 1);
                    setPraiseText(FormatUtils.getInstance().decimalSimplifyFormatter(commentCardData.getComment().getPraiseNumber()));
                } else {
                    commentCardData.setIsPraised(true);
                    commentCardData.getComment().setPraiseNumber(commentCardData.getComment().getPraiseNumber() + 1);
                    setPraiseText(FormatUtils.getInstance().decimalSimplifyFormatter(commentCardData.getComment().getPraiseNumber()));
                }
                praiseButton.onClick(null);
                break;
        }
    }
}
