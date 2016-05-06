package com.test.wllnon.sirenxinyi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.adapter.ItemTouchListenerAdapter;
import com.test.wllnon.sirenxinyi.adapter.RecyclerViewAdapter;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.listener.DragDropTouchListener;
import com.test.wllnon.sirenxinyi.listener.SwipeToDismissTouchListener;
import com.test.wllnon.sirenxinyi.pojo.Answer;
import com.test.wllnon.sirenxinyi.pojo.Question;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;
import com.test.wllnon.sirenxinyi.viewdata.AnswerCardData;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewholder.AnswerCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionAnswerFragment extends BaseTabFragment
        implements ItemTouchListenerAdapter.RecyclerViewOnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CollectionAnswerRecyclerAdapter adapter;

    private DragDropTouchListener dragDropTouchListener;

    private List<BaseCardData> dataList;

    public CollectionAnswerFragment() {
        title = "Collection";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_answer, container, false);
        findViews(rootView);
        setListeners();
        configViews();

        updateFromNetwork();
        return rootView;
    }

    private void findViews(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_collection);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_collection);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);

        dragDropTouchListener = new DragDropTouchListener(recyclerView, getActivity()) {
            @Override
            protected void onItemSwitch(RecyclerView recyclerView, int from, int to) {
                adapter.swapPositions(from, to);
                adapter.clearSelection(from);
            }

            @Override
            protected void onItemDrop(RecyclerView recyclerView, int position) {
                dataList.get(position).setIsVisibility(true);
            }
        };
        recyclerView.addOnItemTouchListener(dragDropTouchListener);
        recyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView, this));
        recyclerView.addOnItemTouchListener(new SwipeToDismissTouchListener(recyclerView,
                new SwipeToDismissTouchListener.DismissCallbacks() {
                    @Override
                    public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                        return SwipeToDismissTouchListener.SwipeDirection.LEFT;
                    }

                    @Override
                    public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
                        for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                            adapter.removeItem(data.position);
                        }
                    }
                }
        ));
    }

    private void configViews() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        adapter = new CollectionAnswerRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        for (int i = 0; i < 10; ++i) {
            AnswerCardData cardData = new AnswerCardData();
            User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            Question question = new Question(0, new String[] {}, "Why I feel so lonely? Is that right? I feel so bad.", getResources().getString(R.string.large_text), 42, 1203, new Time(System.currentTimeMillis()));
            Answer answer = new Answer(0, "Collection", getResources().getString(R.string.large_text), new Time(System.currentTimeMillis()), 1540);
            cardData.setCardKind(Constant.ANSWER);
            cardData.setUser(user);
            cardData.setAnswer(answer);
            cardData.setQuestion(question);
            dataList.add(cardData);
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        dragDropTouchListener.startDrag();
        dataList.get(position).setIsVisibility(false);
    }

    @Override
    public void onRefresh() {
        Application.getInstance().getNetworkUtils().gsonRequest(this,
                UrlUtils.COLLECTION_ANSWER_REFRESH_URL,
                CardDataListTypeUtils.newInstance().getType(),
                new Response.Listener<List<BaseCardData>>() {
                    @Override
                    public void onResponse(List<BaseCardData> response) {
                        AnswerCardData cardData = new AnswerCardData();
                        User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                        Question question = new Question(0, new String[]{}, "Why I feel so lonely? Is that right? I feel so bad.", getResources().getString(R.string.large_text), 42, 1203, new Time(System.currentTimeMillis()));
                        Answer answer = new Answer(0, "Hot Answer", getResources().getString(R.string.large_text), new Time(System.currentTimeMillis()), 1540);
                        cardData.setCardKind(Constant.ANSWER);
                        cardData.setUser(user);
                        cardData.setAnswer(answer);
                        cardData.setQuestion(question);
                        adapter.insertItem(cardData, 0);
                        swipeRefreshLayout.setRefreshing(false);

                        recyclerView.smoothScrollToPosition(0);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Sorry, something wrong with the Internet.", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public class CollectionAnswerRecyclerAdapter extends RecyclerViewAdapter<BaseCardViewHolder> {

        @Override
        public void swapPositions(int from, int to) {
            Collections.swap(dataList, from, to);
            adapter.notifyItemChanged(to);
            adapter.notifyItemChanged(from);
        }

        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.ANSWER:
                    viewHolder = new AnswerCardViewHolder(getActivity(), LayoutInflater.from(getActivity())
                            .inflate(R.layout.cardview_normal, container, false));
                    break;
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataList.size() > position && position >= 0) {
                return dataList.get(position).getCardKind();
            }
            return -1;
        }

        @Override
        public void onBindViewHolder(BaseCardViewHolder viewHolder, int position) {
            viewHolder.setCardData(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void removeItem(int position) {
            dataList.remove(position);
            notifyItemRemoved(position);
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

