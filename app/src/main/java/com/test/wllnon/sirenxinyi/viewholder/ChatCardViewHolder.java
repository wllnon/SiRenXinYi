package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.pojo.AudioMessage;
import com.test.wllnon.sirenxinyi.pojo.ImageMessage;
import com.test.wllnon.sirenxinyi.utils.AudioUtils;
import com.test.wllnon.sirenxinyi.utils.FormatUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.ChatCardData;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ChatCardViewHolder extends BaseCardViewHolder
        implements View.OnClickListener {

    private static final String TAG = "ChatCardViewHolder";

    private TextView content;
    private ImageView picture;
    private TextView time;
    private CircleImageView avatar;


    private TextView audioTV;
    private ImageView audioIM;
    private LinearLayout audio;

    private ProgressBar progressWheel;
    private ImageButton warningButton;

    private ChatCardData chatCardData;

    private boolean isSending = false;
    private boolean isPlaying = false;

    public ChatCardViewHolder(Activity activity, View view) {
        super(activity, Constant.CHAT, view);
        findViews(view);
        setListeners();
    }

    public void findViews(View view) {
        content = (TextView) view.findViewById(R.id.content_chat);
        picture = (ImageView) view.findViewById(R.id.picture_chat);
        time = (TextView) view.findViewById(R.id.time_chat);
        avatar = (CircleImageView) view.findViewById(R.id.avatar_chat);

        audio = (LinearLayout) view.findViewById(R.id.audio_chat);
        audioTV = (TextView) view.findViewById(R.id.audio_tv_chat);
        audioIM = (ImageView) view.findViewById(R.id.audio_im_chat);

        progressWheel = (ProgressBar) view.findViewById(R.id.progress_item);
        warningButton = (ImageButton) view.findViewById(R.id.warning_item);
    }

    public void setListeners() {
        warningButton.setOnClickListener(this);
        audio.setOnClickListener(this);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        if (baseCardData == null) {
            return;
        }
        chatCardData = (ChatCardData) baseCardData;
        switch (chatCardData.getMessage().getType()) {
            case TEXT_MESSAGE:
                messageProcess();
                break;
            case AUDIO_MESSAGE:
                audioMessageProcess();
                break;
            case PICTURE_MESSAGE:
                pictureProcess();
                break;
        }

        setTimeText(FormatUtils.getInstance().timeFormatter(chatCardData.getMessage().getTime()));
        setAvatarUrl(chatCardData.getUser().getAvatarUrl());
        setWarningState(chatCardData.isSent());
        setCardKind(chatCardData.getCardKind());
    }

    private void messageProcess() {
        content.setVisibility(View.VISIBLE);
        picture.setVisibility(View.GONE);
        audio.setVisibility(View.GONE);

        setContentText(chatCardData.getMessage().getContent());
    }

    private void audioMessageProcess() {
        content.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);
        audio.setVisibility(View.VISIBLE);

        AudioMessage message = (AudioMessage) chatCardData.getMessage();
        setAudioText(FormatUtils.getInstance().durationFormatter(message.getLength()));
    }

    private void pictureProcess() {
        content.setVisibility(View.GONE);
        picture.setVisibility(View.VISIBLE);
        audio.setVisibility(View.GONE);

        ImageMessage message = (ImageMessage) chatCardData.getMessage();
        Glide.with(getActivity())
                .load(message.uri)
                .error(R.drawable.ic_broken_image_cyan_500_48dp)
                .crossFade(1200)
                .into(picture);

        // setPictureContent(message.getFilePath());
    }

    public void setContentText(String contentText) {
        if (content != null) {
            content.setText(contentText);
        }
    }

    public void setAudioText(String audioTextt) {
        if (audioTV != null) {
            audioTV.setText(audioTextt);
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

    public void setPictureContent(String imagePath) {
        if (picture != null) {
            Glide.with(getActivity())
                    .load(imagePath)
                    .error(R.drawable.ic_broken_image_cyan_500_48dp)
                    .crossFade(1200)
                    .into(picture);
        }
    }

    private void setWarningState(boolean isSent) {
        if (warningButton != null) {
            if (isSent) {
                stopResend(true);
            } else {
                onClick(warningButton);
            }
        }
    }

    public void startResend() {
        progressWheel.setVisibility(View.VISIBLE);
        warningButton.setVisibility(View.GONE);

        NIMClient.getService(MsgService.class)
                .sendMessage(chatCardData.getMessage().getIMMessage(), true)
                .setCallback(requestCallback);

        isSending = true;
    }

    public void stopResend(boolean success) {
        progressWheel.setVisibility(View.GONE);
        warningButton.setVisibility(success ? View.GONE : View.VISIBLE);

        chatCardData.setSent(success);
        isSending = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.warning_item:
                if (!isSending) {
                    startResend();
                }
                break;

            case R.id.audio_chat:
                if (!isPlaying) {
                    AudioUtils.getInstance().startPlayAudio(getActivity(),
                            ((AudioMessage) chatCardData.getMessage()).getFilePath(),
                            audioTV,
                            audioIM);
                }
                break;
        }
    }

    private RequestCallback<Void> requestCallback = new RequestCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            stopResend(true);
        }

        @Override
        public void onFailed(int i) {
            stopResend(false);
            Toast.makeText(getActivity(), "发送失败。。, 错误代号 " + i, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onException(Throwable throwable) {
            stopResend(false);
            Toast.makeText(getActivity(), "发送异常。。", Toast.LENGTH_SHORT).show();
        }
    };

}
