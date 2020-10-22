package com.vo1d.schedulemanager.v2.ui.schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.ui.classes.list.ClassesListFragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class DaysAdapter extends FragmentStateAdapter {

    private final Map<Integer, ClassesListFragment> fragments = new HashMap<>();
    private List<Day> days = new LinkedList<>();

    public DaysAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ClassesListFragment fragment = new ClassesListFragment(days.get(position).getId());
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void submitList(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    public List<Day> getCurrentList() {
        return days;
    }

    public void removeData(Day data) {
        days.remove(data);
        notifyDataSetChanged();
    }

    public void startEditionModeOnTab(int tabIndex) {
        fragments.get(tabIndex).startEditionMode();
    }
}