package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.ChatActivity;
import com.test.wllnon.sirenxinyi.activity.PersonalInfoActivity;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.MessageCardData;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MessageCardViewHolder extends BaseCardViewHolder
        implements View.OnClickListener {

    private TextView name;
    private TextView content;
    private TextView time;
    private CircleImageView avatar;

    private CardView cardView;

    private MessageCardData messageCardData;

    public MessageCardViewHolder(Activity activity, View view) {
        super(activity, Constant.MESSAGE, view);

        name = (TextView) view.findViewById(R.id.name_message_cardview);
        content = (TextView) view.findViewById(R.id.content_message_cardview);
        time = (TextView) view.findViewById(R.id.time_message_cardview);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_message_cardview);

        cardView = (CardView) view.findViewById(R.id.message_cardview);

        avatar.setOnClickListener(this);
        cardView.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        if (baseCardData == null) {
            return;
        }

        messageCardData = (MessageCardData) baseCardData;
        setNameText(messageCardData.getUser().getName());
        setTimeText(FormatUtils.getInstance().timeFormatter(messageCardData.getTextMessage().getTime()));
        setContentText(messageCardData.getTextMessage().getContent());
        setAvatarUrl(messageCardData.getUser().getAvatarUrl());
        setCardKind(messageCardData.getCardKind());
    }

    public void setNameText(String nameText) {
        if (name != null) {
            name.setText(nameText);
        }
    }

    public void setContentText(String contentText) {
        if (content != null) {
            content.setText(contentText);
        }
    }

    public void setTimeText(String timeText) {
        if (time != null) {
            time.setText(timeText);
        }
    }

    public void setAvatarUrl(String avatarUrl) {
        if (avatar != null) {
            Glide.with(activity)
                    .load(avatarUrl)
                    .error(R.drawable.ic_account_circle_grey_600_48dp)
                    .crossFade(1200)
                    .into(avatar);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.message_cardview:
                intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(messageCardData.getUser()));
                break;
            case R.id.avatar_message_cardview:
                intent = new Intent(activity, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(messageCardData.getUser()));
                break;
        }
        activity.startActivity(intent);
    }
}
