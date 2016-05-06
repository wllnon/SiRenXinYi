package com.test.wllnon.sirenxinyi.fragment;

import android.os.Bundle;
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
import com.test.wllnon.sirenxinyi.adapter.RecyclerViewAdapter;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.listener.SwipeToDismissTouchListener;
import com.test.wllnon.sirenxinyi.pojo.TextMessage;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.MessageCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.MessageCardViewHolder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MessageFragment extends BaseTabFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MessageRecyclerAdapter adapter;

    private List<BaseCardData> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        findViews(rootView);
        setListeners();
        configViews();

        updateCardDataFromNetwork();
        return rootView;
    }

    private void findViews(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_message);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_message);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        // set listener for item swiped
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new MessageRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void updateCardDataFromNetwork() {
        // TODO do some update network.
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        for (int i = 0; i < 8; ++i) {
            MessageCardData cardData = new MessageCardData();
            cardData.setCardKind(Constant.MESSAGE);
            User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            TextMessage textMessage = new TextMessage(0, "Our destiny offers not the cup of despair, but the chalice of opportunity. So let us seize it, not in fear, but in gladness.", new Time(System.currentTimeMillis()));
            cardData.setUser(user);
            cardData.setTextMessage(textMessage);
            dataList.add(cardData);
        }
    }

    @Override
    public void onRefresh() {
        Application.getInstance().getNetworkUtils().gsonRequest(this,
                UrlUtils.MESSAGE_REFRESH_URL,
                CardDataListTypeUtils.newInstance().getType(),
                new Response.Listener<List<BaseCardData>>() {
                    @Override
                    public void onResponse(List<BaseCardData> response) {
                        MessageCardData cardData = new MessageCardData();
                        User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                        TextMessage textMessage = new TextMessage(0, "Our destiny offers not the cup of despair, but the chalice of opportunity. So let us seize it, not in fear, but in gladness.", new Time(System.currentTimeMillis()));
                        cardData.setCardKind(Constant.MESSAGE);
                        cardData.setUser(user);
                        cardData.setTextMessage(textMessage);
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

    public class MessageRecyclerAdapter extends RecyclerViewAdapter<BaseCardViewHolder> {

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
                case Constant.MESSAGE:
                    viewHolder = new MessageCardViewHolder(getActivity(), LayoutInflater.from(getActivity())
                            .inflate(R.layout.cardview_message, container, false));
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

        public void insertItems(List<BaseCardData> datas, int position) {
            dataList.addAll(position, datas);
            notifyItemRangeInserted(position, datas.size());
        }

        public void insertItem(BaseCardData data, int position) {
            dataList.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
