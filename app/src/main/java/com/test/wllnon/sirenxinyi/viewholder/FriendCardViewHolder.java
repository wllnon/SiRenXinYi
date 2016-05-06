package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.ChatActivity;
import com.test.wllnon.sirenxinyi.activity.PersonalInfoActivity;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.FriendCardData;

/**
 * Created by Administrator on 2016/2/22.
 */
public class FriendCardViewHolder extends BaseCardViewHolder
        implements View.OnClickListener {

    private TextView name;
    private TextView simpleIntro;
    private CircleImageView avatar;
    private ImageButton chat;

    private CardView cardView;

    private FriendCardData friendCardData;

    public FriendCardViewHolder(Activity activity, View view) {
        super(activity, Constant.FRIEND, view);
        findViews(view);
        setListeners();
    }

    public void findViews(View view) {
        name = (TextView) view.findViewById(R.id.name_friend_item);
        simpleIntro = (TextView) view.findViewById(R.id.signature_friend_item);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_friend_item);
        chat = (ImageButton) view.findViewById(R.id.chat_friend_item);

        cardView = (CardView) view.findViewById(R.id.friend_item);
    }

    public void setListeners() {
        cardView.setOnClickListener(this);
        chat.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        friendCardData = (FriendCardData) baseCardData;
        if (friendCardData == null) {
            return;
        }
        setNameText(friendCardData.getUser().getName());
        setSimpleIntroText(friendCardData.getUser().getSignature());
        setAvatarUrl(friendCardData.getUser().getAvatarUrl());
        setCardKind(friendCardData.getCardKind());
    }

    public void setNameText(String nameText) {
        if (name != null) {
            name.setText(nameText);
        }
    }

    public void setSimpleIntroText(String simpleIntroText) {
        if (simpleIntro != null) {
            simpleIntro.setText(simpleIntroText);
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
            case R.id.friend_item:
                intent = new Intent(activity, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(friendCardData.getUser()));
                break;
            case R.id.chat_friend_item:
                intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(friendCardData.getUser()));
                break;
        }
        activity.startActivity(intent);
    }
}
