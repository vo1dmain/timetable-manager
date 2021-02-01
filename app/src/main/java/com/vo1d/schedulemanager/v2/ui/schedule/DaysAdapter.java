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
import java.util.Objects;

class DaysAdapter extends FragmentStateAdapter {

    private final Map<Integer, ClassesListFragment> fragments = new HashMap<>();
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
        ClassesListFragment fragment = new ClassesListFragment(id);
        fragments.put(position, fragment);
        return fragment;
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
            fragments.clear();
            ids.clear();
            for (Day day : this.days) {
                ids.add(day.id);
            }
            notifyDataSetChanged();
        }
    }

    public void startEditionModeOnTab(int tabIndex) {
        Objects.requireNonNull(fragments.get(tabIndex)).startEditionMode();
    }
}