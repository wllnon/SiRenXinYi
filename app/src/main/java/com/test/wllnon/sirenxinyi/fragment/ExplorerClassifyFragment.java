package com.test.wllnon.sirenxinyi.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.ClassifyCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.ClassifyCardViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 */
public class ExplorerClassifyFragment extends BaseTabFragment {
    private RecyclerView recyclerView;

    private List<BaseCardData> dataList;

    public ExplorerClassifyFragment() {
        title = "Classify";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classification_explorer, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_classification);

        updateFromNetwork();

        recyclerView.setAdapter(new ClassifyRecyclerAdapter());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        return rootView;
    }

    public void updateFromNetwork() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        int[] colors = new int[] {
                Color.parseColor("#03A9F4"), Color.parseColor("#8BC34A"), Color.parseColor("#9E9E9E"),
                Color.parseColor("#607D8B"), Color.parseColor("#00BCD4"), Color.parseColor("#5677FC"),
                Color.parseColor("#B388FF"), Color.parseColor("#84FFFF"), Color.parseColor("#CDDC39"),
                Color.parseColor("#72d572")
        };
        for (int i = 0; i < 20; ++i) {
            ClassifyCardData cardData = new ClassifyCardData();
            cardData.setCardKind(Constant.CLASSIFY);
            cardData.setTag("2.4K");
            cardData.setTitle("CLASS KIND");
            cardData.setColor(colors[i % colors.length]);
            dataList.add(cardData);
        }
    }

    public class ClassifyRecyclerAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.CLASSIFY:
                    viewHolder = new ClassifyCardViewHolder(getActivity(), LayoutInflater.from(getActivity())
                            .inflate(R.layout.cardview_classify_note, container, false));
                    break;
                case Constant.FOOTER:
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
    }
}
