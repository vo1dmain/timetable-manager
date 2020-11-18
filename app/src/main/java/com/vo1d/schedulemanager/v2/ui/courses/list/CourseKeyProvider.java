package com.vo1d.schedulemanager.v2.ui.courses.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

class CourseKeyProvider extends ItemKeyProvider<Long> {

    private final CoursesListAdapter adapter;
    private final RecyclerView recyclerView;

    public CourseKeyProvider(CoursesListAdapter adapter, RecyclerView recyclerView) {
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
