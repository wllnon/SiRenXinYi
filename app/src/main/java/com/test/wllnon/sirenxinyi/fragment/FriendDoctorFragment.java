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
import com.test.wllnon.sirenxinyi.adapter.ItemTouchListenerAdapter;
import com.test.wllnon.sirenxinyi.adapter.RecyclerViewAdapter;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.listener.DragDropTouchListener;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;
import com.test.wllnon.sirenxinyi.utils.network.UrlUtils;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.FriendCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.FooterCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.FriendCardViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/2/22.
 */
public class FriendDoctorFragment extends BaseTabFragment
        implements ItemTouchListenerAdapter.RecyclerViewOnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DoctorRecyclerAdapter adapter;

    private DragDropTouchListener dragDropTouchListener;

    private List<BaseCardData> dataList;

    public FriendDoctorFragment() {
        title = "Doctor";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctor_friend, container, false);
        findViews(rootView);
        setListeners();
        configViews();

        updateFromNetwork();
        return rootView;
    }

    private void findViews(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_doctor);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_doctor);
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
    }

    private void configViews() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new DoctorRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickView, int position) {
        if (adapter.isFooter(position)) {
            return;
        }
        dragDropTouchListener.startDrag();
        dataList.get(position).setIsVisibility(false);
    }

    // TODO
    public void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        for (int i = 0; i < 20; ++i) {
            FriendCardData cardData = new FriendCardData();
            cardData.setCardKind(Constant.FRIEND);
            User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
            cardData.setUser(user);
            dataList.add(cardData);
        }
    }

    @Override
    public void onRefresh() {
        Application.getInstance().getNetworkUtils().gsonRequest(this,
                UrlUtils.EXPLORER_ANSWER_URL,
                CardDataListTypeUtils.newInstance().getType(),
                new Response.Listener<List<BaseCardData>>() {
                    @Override
                    public void onResponse(List<BaseCardData> response) {
                        FriendCardData cardData = new FriendCardData();
                        User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                        cardData.setCardKind(Constant.FRIEND);
                        cardData.setUser(user);
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

    //public class DoctorRecyclerAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
    public class DoctorRecyclerAdapter extends RecyclerViewAdapter<BaseCardViewHolder> {

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
                case Constant.FRIEND:
                    viewHolder = new FriendCardViewHolder(getActivity(), LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_friend, container, false));
                    break;
                case Constant.FOOTER:
                    viewHolder = new FooterCardViewHolder(getActivity(), LayoutInflater.from(getActivity())
                            .inflate(R.layout.cardview_footer, container, false));
                    break;
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (isFooter(position)) {
                return Constant.FOOTER;
            }
            if (dataList.size() > position && position >= 0) {
                return dataList.get(position).getCardKind();
            }
            return -1;
        }

        @Override
        public void onBindViewHolder(BaseCardViewHolder viewHolder, int position) {
            viewHolder.setCardData(dataList.get(position));
            if (isFooter(position)) {
                final FooterCardViewHolder footerCardViewHolder = (FooterCardViewHolder) viewHolder;
                footerCardViewHolder.setRefreshing(true);
                Application.getInstance().getNetworkUtils().gsonRequest(this,
                        UrlUtils.FRIENDS_FELLOW_URL,
                        CardDataListTypeUtils.newInstance().getType(),
                        new Response.Listener<List<BaseCardData>>() {
                            @Override
                            public void onResponse(List<BaseCardData> response) {
                                if (response == null || response.size() <= 0) {
                                    footerCardViewHolder.setText("Sorry, no more data.");
                                } else {
                                    List<BaseCardData> cardDatas1 = new ArrayList<>();
                                    FriendCardData cardData = new FriendCardData();
                                    User user = new User(0, "San", "I am just a little boy", "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg", false);
                                    cardData.setCardKind(Constant.FRIEND);
                                    cardData.setUser(user);
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
        public int getItemCount() {
            return dataList.size();
        }

        public boolean isFooter(int position) {
            return position + 1 == dataList.size();
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
