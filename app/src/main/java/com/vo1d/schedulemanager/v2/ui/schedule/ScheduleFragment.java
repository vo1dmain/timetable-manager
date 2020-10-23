package com.vo1d.schedulemanager.v2.ui.schedule;

import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassViewModel;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;
import com.vo1d.schedulemanager.v2.data.days.DaysViewModel;
import com.vo1d.schedulemanager.v2.data.weeks.WeekViewModel;
import com.vo1d.schedulemanager.v2.data.weeks.WeekWithDays;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;
import com.vo1d.schedulemanager.v2.ui.dialogs.ListDialog;

import java.util.Collections;
import java.util.List;

import static com.vo1d.schedulemanager.v2.MainActivity.getActionMode;


public class ScheduleFragment extends Fragment {

    private ViewPager2 vp2;
    private WeekViewModel wvm;
    private DaysViewModel dvm;
    private DaysAdapter adapter;
    private LinearLayout tabStrip;
    private String[] daysNamesShort;
    private String[] daysNamesFull;
    private Day tagToDelete;
    private View tabToDelete;
    private Resources resources;

    private WeekWithDays currentWeek;

    private LiveData<WeekWithDays> currentWeekLive;

    private int currentDayNumber;
    private MenuItem actionAdd;
    private ClassViewModel cvm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Calendar calendar = Calendar.getInstance();
        currentDayNumber = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        resources = requireActivity().getResources();
        daysNamesShort = resources.getStringArray(R.array.days_of_week_short);
        daysNamesFull = resources.getStringArray(R.array.days_of_week_full);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        wvm = provider.get(WeekViewModel.class);
        dvm = provider.get(DaysViewModel.class);
        cvm = provider.get(ClassViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        vp2 = view.findViewById(R.id.view_pager);

        tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        adapter = new DaysAdapter(this);
        vp2.setAdapter(adapter);

        vp2.setOffscreenPageLimit(3);

        currentWeekLive = wvm.findWeekById(1);

        currentWeekLive.observe(getViewLifecycleOwner(), weekWithDays -> {
            currentWeek = weekWithDays;
            adapter.submitList(currentWeek.days);
            vp2.setCurrentItem(currentDayNumber, false);

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                registerForContextMenu(tabStrip.getChildAt(i));
                tabStrip.getChildAt(i).setTag(currentWeek.days.get(i));
            }
        });

        TabLayoutMediator defaultTabMediator = new TabLayoutMediator(tabLayout, vp2, (tab, position) ->
                tab.setText(daysNamesShort[currentWeek.days.get(position).order])
        );

        defaultTabMediator.attach();

        if (savedInstanceState != null) {
            vp2.setCurrentItem(savedInstanceState.getInt("tabPosition"), false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedule, menu);

        actionAdd = menu.findItem(R.id.add_action);

        currentWeekLive.observe(getViewLifecycleOwner(), weekWithDays ->
                actionAdd.setEnabled(!weekWithDays.week.availableDays.isEmpty()));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_edition_mode_action) {
            adapter.startEditionModeOnTab(vp2.getCurrentItem());
            return true;
        } else if (id == R.id.add_action) {
            openSelectionDialog(currentWeek.week.availableDays);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (vp2 != null)
            outState.putInt("tabPosition", vp2.getCurrentItem());
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_tab_context, menu);

        tagToDelete = (Day) v.getTag();
        tabToDelete = v;

        ActionMode mode = getActionMode();
        if (mode != null) {
            mode.finish();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.delete_day) {
            openConfirmationDialog();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void openConfirmationDialog() {
        setupDeleteTabDialog().show(getParentFragmentManager(), "Delete tab confirmation");
    }

    private void openSelectionDialog(List<DaysOfWeek> availableDays) {
        new ListDialog(R.string.dialog_title_select_day, availableDays, (dialog, itemNumber) -> {
            dvm.insert(new Day(availableDays.get(itemNumber), currentWeek.week.id));
            availableDays.remove(itemNumber);
            wvm.update(currentWeek.week);
        })
                .show(getParentFragmentManager(), "Select day dialog");
    }

    private ConfirmationDialog setupDeleteTabDialog() {
        int titleId = R.string.dialog_title_delete_one;
        String title = daysNamesFull[tagToDelete.order];

        String message = resources.getString(R.string.dialog_message_delete_tab, title);
        String snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Snackbar s = Snackbar.make(requireView(), snackbarMes, Snackbar.LENGTH_LONG);

                Class[] classes = cvm.findAllClassesForADayAsArray(tagToDelete.id);

                s.setAction(R.string.snackbar_action_undone, v -> restoreDeletedDay(tagToDelete, classes));

                deleteSelectedTab();

                s.show();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        };

        ConfirmationDialog dialog = new ConfirmationDialog(titleId, message);
        dialog.setDialogListener(listener);

        return dialog;
    }

    private void deleteSelectedTab() {
        dvm.delete(tagToDelete);
        unregisterForContextMenu(tabToDelete);
        currentWeek.week.availableDays.add(DaysOfWeek.fromInt(tagToDelete.order));
        Collections.sort(currentWeek.week.availableDays);
        wvm.update(currentWeek.week);
    }

    private void restoreDeletedDay(Day day, Class[] classes) {
        dvm.insert(day);
        cvm.insert(classes);
        currentWeek.week.availableDays.remove(DaysOfWeek.fromInt(tagToDelete.order));
        wvm.update(currentWeek.week);
    }
}