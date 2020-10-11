package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

final class ClassDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView mRecyclerView;

    public ClassDetailsLookup(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof ClassesListAdapter.ViewHolder) {
                final ClassesListAdapter.ViewHolder journalHolder = (ClassesListAdapter.ViewHolder) holder;

                return new ItemDetails<Long>() {
                    @Override
                    public int getPosition() {
                        return journalHolder.getAdapterPosition();
                    }

                    @NonNull
                    @Override
                    public Long getSelectionKey() {
                        return journalHolder.getItemId();
                    }
                };
            }
        }
        return null;
    }

}
