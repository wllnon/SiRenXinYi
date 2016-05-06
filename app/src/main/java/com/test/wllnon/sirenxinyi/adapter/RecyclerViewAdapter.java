package com.test.wllnon.sirenxinyi.adapter;

/**
 * Created by Administrator on 2016/2/23.
 */
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension to standard RecyclerView.Adapter that also keep state of selected/activated items.
 *
 * @param <H> - ViewHolder type
 */
public abstract class RecyclerViewAdapter<H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void swapPositions(int from, int to) {
    }

    @Override
    public void onBindViewHolder(H viewHolder, int position) {
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void setSelected(int pos) {
        selectedItems.put(pos, true);
        notifyItemChanged(pos);
    }

    public void clearSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        if (selectedItems.size() > 0) {
            selectedItems.clear();
            notifyDataSetChanged();
        }
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItemsPositions() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

}
