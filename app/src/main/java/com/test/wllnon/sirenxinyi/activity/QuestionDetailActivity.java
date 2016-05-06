package com.test.wllnon.sirenxinyi.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.listener.RecyclerViewOnScrollChangedListener;
import com.test.wllnon.sirenxinyi.listener.ScrollViewOnScrollChangedListener;
import com.test.wllnon.sirenxinyi.pojo.Answer;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;
import com.test.wllnon.sirenxinyi.viewdata.AnswerCardData;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.QuestionCardData;
import com.test.wllnon.sirenxinyi.viewholder.AnswerCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.FooterCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.QuestionHeaderCardViewHolder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class QuestionDetailActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;

    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private QuestionRecyclerAdapter adapter;

    private List<BaseCardData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        findViews();
        setListeners();
        configViews();
        updateFromNetwork();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_question_detail);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_question_detail);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_question_detail);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new RecyclerViewOnScrollChangedListener
                .Builder(ScrollViewOnScrollChangedListener.ViewType.FOOTER)
                .footer(floatingActionButton)
                .minFooterTranslation(getResources().getDimensionPixelSize(R.dimen.fab_size_normal)
                        + getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .isSnappable(true)
                .build()
        );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/4/25
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

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        recyclerView.setLayoutManager(new LinearLayoutManager(QuestionDetailActivity.this));
        recyclerView.setHasFixedSize(true);

        adapter = new QuestionRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        String json = getIntent().getStringExtra("data");
        QuestionCardData questionCardData = new QuestionCardData();
        if (json == null) {
            json = getIntent().getStringExtra("question");
            questionCardData.setQuestion(GsonUtils.newInstance().getGson().fromJson(json, Question.class));
        } else {
            questionCardData.setQuestion(GsonUtils.newInstance().getGson().fromJson(json, QuestionCardData.class).getQuestion());
        }
        questionCardData.setUser(Application.getInstance().getUser());
        questionCardData.setCardKind(Constant.QUESTION);
        dataList.add(questionCardData);

        for (int i = 0; i < 10; ++i) {
            AnswerCardData cardData = new AnswerCardData();
            User user = new User(0, "San" + i, "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            Question question = new Question(0, new String[] {}, "Why I feel so lonely? Is that right? I feel so bad.", getResources().getString(R.string.large_text), 42, 1203, new Time(System.currentTimeMillis()));
            Answer answer = new Answer(0, "Some Answer", getResources().getString(R.string.large_text), new Time(System.currentTimeMillis()), 1540);
            cardData.setCardKind(Constant.ANSWER);
            cardData.setUser(user);
            cardData.setAnswer(answer);
            cardData.setQuestion(question);
            dataList.add(cardData);
        }
    }

    @Override
    public void onRefresh() {
        // TODO: 2016/4/25 no-op
        swipeRefreshLayout.setRefreshing(false);
    }

    public class QuestionRecyclerAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.ANSWER:
                    viewHolder = new AnswerCardViewHolder(QuestionDetailActivity.this, LayoutInflater.from(QuestionDetailActivity.this)
                            .inflate(R.layout.cardview_normal, container, false));
                    break;
                case Constant.FOOTER:
                    viewHolder = new FooterCardViewHolder(QuestionDetailActivity.this, LayoutInflater.from(QuestionDetailActivity.this)
                            .inflate(R.layout.cardview_footer, container, false));
                    break;
                case Constant.HEADER:
                    viewHolder = new QuestionHeaderCardViewHolder(QuestionDetailActivity.this, LayoutInflater.from(QuestionDetailActivity.this)
                            .inflate(R.layout.cardview_question_header, container, false));
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BaseCardViewHolder viewHolder, int position) {
            viewHolder.setCardData(dataList.get(position));
            if (isFooter(position)) {
                final FooterCardViewHolder footerCardViewHolder = (FooterCardViewHolder) viewHolder;
                footerCardViewHolder.setRefreshing(true);
                Application.getInstance().getNetworkUtils().gsonRequest(QuestionDetailActivity.this,
                        UrlUtils.HOME_FRAGMENT_URL,
                        CardDataListTypeUtils.newInstance().getType(),
                        new Response.Listener<List<BaseCardData>>() {
                            @Override
                            public void onResponse(List<BaseCardData> response) {
                                if (response == null || response.size() <= 0) {
                                    footerCardViewHolder.setText("Sorry, no more data.");
                                } else {
                                    List<BaseCardData> cardDatas1 = new ArrayList<>();
                                    AnswerCardData cardData = new AnswerCardData();
                                    User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                                    Question question = new Question(0, new String[] {}, "Why I feel so lonely? Is that right? I feel so bad.", getResources().getString(R.string.large_text), 42, 1203, new Time(System.currentTimeMillis()));
                                    Answer answer = new Answer(0, "Some Answer", getResources().getString(R.string.large_text), new Time(System.currentTimeMillis()), 1540);
                                    cardData.setCardKind(Constant.ANSWER);
                                    cardData.setUser(user);
                                    cardData.setAnswer(answer);
                                    cardData.setQuestion(question);
                                    cardDatas1.add(cardData);
                                    adapter.insertItems(cardDatas1, dataList.size());
                                }
                                // dataList.addAll(response);
                                footerCardViewHolder.setRefreshing(false);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                footerCardViewHolder.setText("Sorry, the network error.");
                                footerCardViewHolder.setRefreshing(false);
                            }
                        });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return Constant.HEADER;
            }
            if (isFooter(position)) {
                return Constant.FOOTER;
            }
            if (dataList.size() > position && position >= 0) {
                return dataList.get(position).getCardKind();
            }
            return -1;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public boolean isFooter(int position) {
            return position + 1 == dataList.size();
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        public void insertItems(List<BaseCardData> datas, int position) {
            dataList.addAll(position, datas);
            notifyItemRangeInserted(position, datas.size());
        }

        public void insertItem(BaseCardData data, int position) {
            dataList.add(position, data);
            notifyItemInserted(position);
        }
    }

}
