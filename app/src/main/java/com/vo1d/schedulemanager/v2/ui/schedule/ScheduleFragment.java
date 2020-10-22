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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.days.DaysViewModel;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ScheduleFragment extends Fragment {

    private static ActionMode actionMode;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ViewPager2 vp2;
    private DaysViewModel dvm;
    private DaysAdapter adapter;
    private LinearLayout tabStrip;
    private String[] daysNames;
    private Day currentTag;
    private View chosenTab;
    private Resources resources;

    private int currentDayNumber;

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
        currentDayNumber = calendar.get(Calendar.DAY_OF_WEEK) - 2;

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

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        vp2 = view.findViewById(R.id.view_pager);

        tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        adapter = new DaysAdapter(this);
        vp2.setAdapter(adapter);

        vp2.setOffscreenPageLimit(3);

        dvm.getAllDays().observe(getViewLifecycleOwner(), days -> {
            adapter.submitList(days);
            vp2.setCurrentItem(currentDayNumber, false);

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                registerForContextMenu(tabStrip.getChildAt(i));
                tabStrip.getChildAt(i).setTag(days.get(i));
            }
        });

        new TabLayoutMediator(tabLayout, vp2, (tab, position) ->
                tab.setText(daysNames[Objects.requireNonNull(dvm.getAllDays().getValue()).get(position).getOrder()])
        ).attach();

        if (savedInstanceState != null) {
            vp2.setCurrentItem(savedInstanceState.getInt("tabPosition"), false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_edition_mode_action) {
            adapter.startEditionModeOnTab(vp2.getCurrentItem());
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
        requireActivity().getMenuInflater().inflate(R.menu.menu_context, menu);

        currentTag = (Day) v.getTag();
        chosenTab = v;

        if (actionMode != null) {
            actionMode.finish();
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

    private ConfirmationDialog setupDeleteTabDialog() {
        int titleId = R.string.dialog_title_delete_one;
        String title = daysNames[currentTag.getOrder()];

        String message = resources.getString(R.string.dialog_message_delete_tab, title);
        String snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Snackbar s = Snackbar.make(requireView(), snackbarMes, 2750);

                ScheduledFuture<?> operation = executor.schedule(
                        ScheduleFragment.this::deleteSelectedItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

                List<Day> backedUpData = adapter.getCurrentList();

                s.setAction(R.string.snackbar_action_undone, v -> {
                    operation.cancel(false);
                    adapter.submitList(backedUpData);
                });

                adapter.removeData(currentTag);

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

    private void deleteSelectedItems() {
        dvm.delete(currentTag);
        unregisterForContextMenu(chosenTab);
    }
}