package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.AudioMessage;
import com.test.wllnon.sirenxinyi.pojo.ImageMessage;
import com.test.wllnon.sirenxinyi.pojo.Message;
import com.test.wllnon.sirenxinyi.pojo.TextMessage;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.ChatCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.ChatCardViewHolder;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final static String TAG = "ChatActivity";

    public final static int REQUEST_AUDIO_RECORD = 1;
    public final static int REQUEST_PICTURE_PIKE = 2;

    public final static int RESULT_CANCEL = 101;
    public final static int RESULT_SUCCESS = 102;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ChatRecyclerAdapter adapter;

    private Toolbar toolbar;
    private EditText chatEditView;
    private ImageButton recordButton;
    private ImageButton sendButton;

    private Drawable sendImage;
    private Drawable actionImage;

    private List<BaseCardData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViews();
        setListeners();
        configViews();

        updateFromNetwork();

        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, false);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_chat);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);

        chatEditView = (EditText) findViewById(R.id.edit_text_chat);
        recordButton = (ImageButton) findViewById(R.id.record_chat);
        sendButton = (ImageButton) findViewById(R.id.send_chat);

        sendImage = getResources().getDrawable(R.drawable.ic_send_black_24dp);
        actionImage = getResources().getDrawable(R.drawable.ic_add_black_24dp);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        recordButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        chatEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                sendModel(!TextUtils.isEmpty(s.toString()));
            }
        });
    }

    private void configViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ChatRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendModel(false);
    }

    private boolean isMsgFromCurrUser(List<IMMessage> messages) {
        return messages != null && messages.size() > 0 && messages.get(0).getFromAccount().equals("1301671535@qq.com");
    }

    private void sendModel(boolean isSendModel) {
        if (isSendModel) {
            sendButton.setTag(true);
            sendButton.setImageDrawable(sendImage);
        } else {
            sendButton.setTag(null);
            sendButton.setImageDrawable(actionImage);
        }
    }

    private boolean isSendModel() {
        return sendButton.getTag() != null;
    }

    private void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        ChatCardData cardData1 = new ChatCardData();
        cardData1.setCardKind(Constant.CHAT);
        cardData1.setUser(Application.getInstance().getUser());
        cardData1.setMessage(new AudioMessage(0, null, null, 0, null, new Time(System.currentTimeMillis())));
        cardData1.setSent(true);
        dataList.add(cardData1);

        for (int i = 0; i < 10; ++i) {
            ChatCardData cardData = new ChatCardData();
            if (i % 2 == 0) {
                cardData.setCardKind(Constant.CHAT);
            } else {
                cardData.setCardKind(Constant.CHAT_LEFT);
            }
            User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            TextMessage textMessage = new TextMessage(0, "Why I feel so lonely? Is that right? I feel so bad.", new Time(System.currentTimeMillis()));
            cardData.setUser(user);
            cardData.setMessage(textMessage);
            cardData.setSent(true);
            dataList.add(cardData);
        }
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }

    private void addCardData(Message message, boolean isInComing) {
        ChatCardData cardData = new ChatCardData();

        cardData.setCardKind(isInComing ? Constant.CHAT_LEFT : Constant.CHAT);
        cardData.setUser(Application.getInstance().getUser());
        cardData.setSent(isInComing);
        cardData.setMessage(message);

        adapter.insertItem(cardData);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void clearEditText() {
        chatEditView.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_chat:
                if (isSendModel()) {
                    addCardData(new TextMessage(0,
                            chatEditView.getText().toString(),
                            new Time(System.currentTimeMillis())
                        ), false
                    );

                    clearEditText();
                } else {
                    startActivityForResult(new Intent(ChatActivity.this, PicturePikerActivity.class), REQUEST_PICTURE_PIKE);
                }
                break;
            case R.id.record_chat:
                startActivityForResult(new Intent(ChatActivity.this, AudioRecordActivity.class), REQUEST_AUDIO_RECORD);
                break;

        }
    }

    @Override
    public void onRefresh() {
        // TODO: 2016/5/2  
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: ");
        switch (requestCode) {
            case REQUEST_AUDIO_RECORD:
                switch (resultCode) {
                    case RESULT_SUCCESS:
                        Log.i(TAG, "onActivityResult: success");
                        addCardData(new AudioMessage(0,
                                (File) data.getExtras().get(AudioRecordActivity.FILE_AUDIO),
                                null,
                                (long) data.getExtras().get(AudioRecordActivity.LENGTH_AUDIO),
                                null,
                                new Time(System.currentTimeMillis())
                            ), false
                        );
                        break;
                    case RESULT_CANCEL:
                        break;
                }
                break;
            case REQUEST_PICTURE_PIKE:
                switch (resultCode) {
                    case RESULT_SUCCESS:
                        addCardData(new ImageMessage(0,
                                ChatActivity.this,
                                data.getData(),
                                new Time(System.currentTimeMillis())
                            ), false
                        );
                        break;
                    case RESULT_CANCEL:
                        Log.i(TAG, "onActivityResult: cancel");
                        break;
                }
        }
    }

    public class ChatRecyclerAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.CHAT:
                    viewHolder = new ChatCardViewHolder(ChatActivity.this, LayoutInflater.from(ChatActivity.this)
                            .inflate(R.layout.item_chat_right, container, false));
                    break;
                case Constant.CHAT_LEFT:
                    viewHolder = new ChatCardViewHolder(ChatActivity.this, LayoutInflater.from(ChatActivity.this)
                            .inflate(R.layout.item_chat_left, container, false));
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BaseCardViewHolder viewHolder, int position) {
            viewHolder.setCardData(dataList.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            if (dataList.size() > position && position >= 0) {
                return dataList.get(position).getCardKind();
            }
            return -1;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void insertItem(BaseCardData data) {
            dataList.add(data);
            notifyItemInserted(dataList.size() - 1);
        }
    }

    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (isMsgFromCurrUser(messages)) {
                for (IMMessage imMessage : messages) {
                    Message message = null;
                    switch (imMessage.getMsgType()) {
                        case audio:
                            AudioAttachment audioAttachment = (AudioAttachment) imMessage.getAttachment();
                            message = new AudioMessage(0, null, audioAttachment.getPath(),
                                    audioAttachment.getDuration(), null, new Time(imMessage.getTime()));
                            break;
                        case image:
                            ImageAttachment imageMessage = (ImageAttachment) imMessage.getAttachment();
                            message = new ImageMessage(0, null, imageMessage.getPath(),
                                    new Time(imMessage.getTime()));
                            break;
                        case text:
                            message = new TextMessage(0, imMessage.getContent(), new Time(imMessage.getTime()));
                            break;
                    }
                    addCardData(message, true);
                }
            }
        }
    };

}
