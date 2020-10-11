package com.vo1d.schedulemanager.v2.ui.schedule;

import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.day.DaysViewModel;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class ScheduleFragment extends Fragment {

    private static ActionMode actionMode;
    private Resources resources;
    private ViewPager2 vp2;
    private TabLayout tabLayout;
    private DaysViewModel dvm;
    private DaysAdapter adapter;

    private String[] daysNames;
    private int currentDay;

    public static ActionMode getActionMode() {
        return actionMode;
    }

    public static void setActionMode(ActionMode actionMode) {
        ScheduleFragment.actionMode = actionMode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        resources = requireActivity().getResources();
        dvm = new ViewModelProvider(requireActivity()).get(DaysViewModel.class);
        daysNames = resources.getStringArray(R.array.days_of_week);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabs);
        vp2 = view.findViewById(R.id.view_pager);

        adapter = new DaysAdapter(this);
        vp2.setAdapter(adapter);

        vp2.setOffscreenPageLimit(3);

        dvm.getAllDays().observe(getViewLifecycleOwner(), days -> {
            adapter.submitList(days);
            vp2.setCurrentItem(currentDay, false);
        });

        new TabLayoutMediator(tabLayout, vp2, (tab, position) ->
                tab.setText(
                        daysNames[
                                Objects.requireNonNull(dvm.getAllDays().getValue())
                                        .get(position).getOrder()
                                ]
                )
        ).attach();

        if (savedInstanceState != null)
            vp2.setCurrentItem(savedInstanceState.getInt("tabPosition"), false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (vp2 != null)
            outState.putInt("tabPosition", vp2.getCurrentItem());
    }
}