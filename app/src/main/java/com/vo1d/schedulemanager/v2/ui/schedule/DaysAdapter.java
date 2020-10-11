package com.vo1d.schedulemanager.v2.ui.schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vo1d.schedulemanager.v2.data.day.Day;
import com.vo1d.schedulemanager.v2.ui.classes.list.ClassesListFragment;

import java.util.LinkedList;
import java.util.List;

class DaysAdapter extends FragmentStateAdapter {

    private List<Day> days = new LinkedList<>();

    public DaysAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ClassesListFragment(days.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void submitList(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }
}