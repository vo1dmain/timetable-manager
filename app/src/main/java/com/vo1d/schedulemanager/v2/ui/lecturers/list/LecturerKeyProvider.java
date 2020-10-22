package com.vo1d.schedulemanager.v2.ui.lecturers.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

class LecturerKeyProvider extends ItemKeyProvider<Long> {

    private final LecturersListAdapter adapter;
    private final RecyclerView recyclerView;

    public LecturerKeyProvider(LecturersListAdapter adapter, RecyclerView recyclerView) {
        super(SCOPE_MAPPED);
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }


    @Nullable
    @Override
    public Long getKey(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(key);
        return viewHolder == null ? RecyclerView.NO_POSITION : viewHolder.getAdapterPosition();
    }
}
