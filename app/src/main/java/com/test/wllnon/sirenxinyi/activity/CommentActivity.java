package com.test.wllnon.sirenxinyi.activity;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.pojo.Comment;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.CommentCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.CommentCardViewHolder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;

    private TextInputEditText commentEditView;
    private ImageButton sendButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CommentRecyclerAdapter adapter;

    private List<BaseCardData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        findViews();
        setListeners();
        settingViews();

        updateFromNetwork();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_comment);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_comment);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comment);

        commentEditView = (TextInputEditText) findViewById(R.id.edit_text_comment);
        sendButton = (ImageButton) findViewById(R.id.send_comment);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        sendButton.setOnClickListener(this);
    }

    private void settingViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        adapter = new CommentRecyclerAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        for (int i = 0; i < 10; ++i) {
            CommentCardData cardData = new CommentCardData();
            User user = new User(0, "San" + i, "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            Comment comment = new Comment(562, i, getResources().getString(R.string.personal_info_intro_default), new Time(System.currentTimeMillis()));
            cardData.setCardKind(Constant.COMMENT);
            cardData.setUser(user);
            cardData.setComment(comment);
            dataList.add(cardData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_comment:
                if (Application.getInstance().getUser() != null) {
                    // TODO: 2016/4/22 do some networks
                    String text = commentEditView.getText().toString();
                    CommentCardData commentCardData = new CommentCardData();
                    commentCardData.setUser(Application.getInstance().getUser());
                    commentCardData.setComment(new Comment(0, 0, text, new Time(System.currentTimeMillis())));
                    commentCardData.setIsPraised(false);
                    commentCardData.setCardKind(Constant.COMMENT);
                    adapter.insertItem(commentCardData);
                    recyclerView.smoothScrollToPosition(0);

                    commentEditView.setText("");
                    commentEditView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(commentEditView.getWindowToken(), 0);
                } else {
                    // TODO: 2016/4/22 jumping to login
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        Application.getInstance().getNetworkUtils().gsonRequest(CommentActivity.this,
                UrlUtils.COMMENT_GET_URL,
                CardDataListTypeUtils.newInstance().getType(),
                new Response.Listener<List<BaseCardData>>() {
                    @Override
                    public void onResponse(List<BaseCardData> response) {
                        // TODO: 2016/4/22 gson the json
                        CommentCardData cardData = new CommentCardData();
                        User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                        Comment comment = new Comment(562, 0, getResources().getString(R.string.personal_info_intro_default), new Time(System.currentTimeMillis()));
                        cardData.setUser(user);
                        cardData.setComment(comment);
                        cardData.setIsPraised(false);
                        adapter.insertItem(cardData);
                        swipeRefreshLayout.setRefreshing(false);

                        recyclerView.smoothScrollToPosition(0);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentActivity.this, "Sorry, something wrong with the Internet.", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public class CommentRecyclerAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.COMMENT:
                    viewHolder = new CommentCardViewHolder(CommentActivity.this, LayoutInflater.from(CommentActivity.this)
                            .inflate(R.layout.item_comment, container, false));
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
            dataList.add(0, data);
            notifyItemInserted(0);
        }
    }
}
