package com.vo1d.schedulemanager.v2.ui.schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.ui.classes.list.ClassesListFragment;

import java.util.LinkedList;
import java.util.List;

class DaysAdapter extends FragmentStateAdapter {
    private final List<Integer> ids = new LinkedList<>();
    private List<Day> days = new LinkedList<>();

    public DaysAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public boolean containsItem(long itemId) {
        return ids.contains((int) itemId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int id = days.get(position).id;
        return new ClassesListFragment(id);
    }

    @Override
    public long getItemId(int position) {
        return ids.get(position);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void submitList(List<Day> days) {
        if (days != this.days) {
            this.days = days;
            ids.clear();
            for (Day day : this.days) {
                ids.add(day.id);
            }
            notifyDataSetChanged();
        }
    }
}