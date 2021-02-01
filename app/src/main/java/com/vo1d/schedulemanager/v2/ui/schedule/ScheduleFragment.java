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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import com.google.android.material.textview.MaterialTextView;
import com.vo1d.schedulemanager.v2.MainActivity;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassViewModel;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;
import com.vo1d.schedulemanager.v2.data.days.DaysViewModel;
import com.vo1d.schedulemanager.v2.data.weeks.Week;
import com.vo1d.schedulemanager.v2.data.weeks.WeekViewModel;
import com.vo1d.schedulemanager.v2.data.weeks.WeekWithDays;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;
import com.vo1d.schedulemanager.v2.ui.dialogs.ListDialog;
import com.vo1d.schedulemanager.v2.ui.dialogs.TextInputDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.vo1d.schedulemanager.v2.MainActivity.getActionMode;


public class ScheduleFragment extends Fragment {
    private final Comparator<WeekWithDays> weekComparator;
    private ArrayAdapter<WeekWithDays> weeksAdapter;
    private ClassViewModel cvm;
    private DaysViewModel dvm;
    private DaysAdapter adapter;

    private LinearLayout tabStrip;

    private MaterialTextView weekIsEmptyTextView;
    private MaterialTextView scheduleIsEmptyTextView;
    private MenuItem actionAddDay;
    private MenuItem actionAddWeek;
    private MenuItem actionDeleteWeek;
    private MenuItem actionEdit;
    private MenuItem actionRenameWeek;

    private Resources resources;

    private ScheduleFragmentViewModel sfvm;

    private View tabToDelete;
    private ViewPager2 vp2;

    private WeekViewModel wvm;

    public ScheduleFragment() {
        super();
        weekComparator = (o1, o2) -> o1.week.compareTo(o2.week);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        wvm = provider.get(WeekViewModel.class);
        dvm = provider.get(DaysViewModel.class);
        cvm = provider.get(ClassViewModel.class);

        sfvm = new ViewModelProvider(this).get(ScheduleFragmentViewModel.class);

        Calendar calendar = Calendar.getInstance();
        sfvm.setCurrentDayNumber(calendar.get(Calendar.DAY_OF_WEEK) - 2);

        resources = requireActivity().getResources();
        sfvm.setDaysNamesShort(resources.getStringArray(R.array.days_of_week_short));
        sfvm.setDaysNamesFull(resources.getStringArray(R.array.days_of_week_full));
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
        weekIsEmptyTextView = view.findViewById(R.id.week_placeholder);
        scheduleIsEmptyTextView = view.findViewById(R.id.schedule_placeholder);

        Spinner weeksSpinner = ((MainActivity) requireActivity()).weeksSpinner;

        tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        weeksAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );

        adapter = new DaysAdapter(this);

        vp2.setAdapter(adapter);
        vp2.setOffscreenPageLimit(3);

        weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weeksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                if (obj != null) {
                    if (obj instanceof WeekWithDays) {
                        sfvm.setCurrentWeekLive((WeekWithDays) obj);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        weeksSpinner.setAdapter(weeksAdapter);

        wvm.getAllWeeks().observe(getViewLifecycleOwner(), weeks -> {
            if (weeks.size() > 0) {
                weeksAdapter.clear();
                scheduleIsEmptyTextView.setVisibility(View.GONE);
                if (weeks.size() > 1) {
                    weeksAdapter.addAll(weeks);
                    weeksAdapter.sort(weekComparator);
                    weeksAdapter.notifyDataSetChanged();
                    weeksSpinner.setVisibility(View.VISIBLE);
                } else {
                    weeksSpinner.setVisibility(View.GONE);
                }

                int currentWeekPosition = weeksSpinner.getSelectedItemPosition();
                sfvm.setCurrentWeekLive(
                        weeks.get(currentWeekPosition == AdapterView.INVALID_POSITION ?
                                0 : currentWeekPosition == weeks.size() ?
                                currentWeekPosition - 1 : currentWeekPosition
                        )
                );
            } else {
                weekIsEmptyTextView.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                scheduleIsEmptyTextView.setVisibility(View.VISIBLE);
            }
        });

        sfvm.getCurrentWeekLive().observe(getViewLifecycleOwner(), week -> {
            if (week.days.size() > 0) {

                tabLayout.setVisibility(View.VISIBLE);
                vp2.setVisibility(View.VISIBLE);
                weekIsEmptyTextView.setVisibility(View.GONE);

                Collections.sort(week.days);
                adapter.submitList(week.days);

                vp2.setCurrentItem(sfvm.getCurrentDayNumber(), false);

                for (int i = 0; i < tabStrip.getChildCount(); i++) {
                    registerForContextMenu(tabStrip.getChildAt(i));
                    tabStrip.getChildAt(i).setTag(week.days.get(i));
                }
            } else {
                tabLayout.setVisibility(View.GONE);
                vp2.setVisibility(View.GONE);
                weekIsEmptyTextView.setVisibility(View.VISIBLE);
            }
        });

        new TabLayoutMediator(tabLayout, vp2, (tab, position) ->
                tab.setText(sfvm.getDayNameForPosition(position))
        ).attach();

        if (savedInstanceState != null) {
            vp2.setCurrentItem(savedInstanceState.getInt("tabPosition"), false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedule, menu);

        actionAddDay = menu.findItem(R.id.add_day_action);
        actionEdit = menu.findItem(R.id.start_edition_mode_action);
        actionAddWeek = menu.findItem(R.id.add_week_action);
        actionDeleteWeek = menu.findItem(R.id.delete_this_week_action);
        actionRenameWeek = menu.findItem(R.id.rename_week_action);

        wvm.getAllWeeks().observe(getViewLifecycleOwner(), weeks -> {
            boolean thereIsWeeks = weeks.size() > 0;

            actionAddDay.setVisible(thereIsWeeks);
            actionDeleteWeek.setVisible(thereIsWeeks);
            actionRenameWeek.setVisible(thereIsWeeks);
            actionEdit.setVisible(thereIsWeeks);

            if (!thereIsWeeks) {
                actionAddWeek.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            } else {
                actionAddWeek.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
        });

        sfvm.getCurrentWeekLive().observe(getViewLifecycleOwner(),
                currentWeek -> actionEdit.setVisible(currentWeek.days.size() > 0));

        sfvm.getCurrentWeekLive().observe(getViewLifecycleOwner(),
                currentWeek -> actionAddDay.setVisible(!currentWeek.week.availableDays.isEmpty()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_edition_mode_action) {
            adapter.startEditionModeOnTab(vp2.getCurrentItem());
            return true;
        } else if (id == R.id.add_day_action) {
            openSelectionDialog(sfvm.getCurrentWeek().week.availableDays);
            return true;
        } else if (id == R.id.add_week_action) {
            openCreationDialog();
            return true;
        } else if (id == R.id.rename_week_action) {
            openEditionDialog();
            return true;
        } else if (id == R.id.delete_this_week_action) {
            openConfirmationDialog(ConfirmationScenario.DeleteWeek);
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
    public void onCreateContextMenu(@NonNull ContextMenu menu,
                                    @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_tab_context, menu);

        sfvm.setDayToDelete((Day) v.getTag());
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
            openConfirmationDialog(ConfirmationScenario.DeleteDay);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void openEditionDialog() {
        TextInputDialog dialog = new TextInputDialog(new TextInputDialog.Listener() {
            @Override
            public void onPositiveClick(DialogFragment dialog) {
                EditText et = Objects.requireNonNull(dialog.getDialog())
                        .findViewById(R.id.new_week_title);

                String title = et.getText().toString().trim();

                Week week = sfvm.getCurrentWeek().week;

                week.title = title;

                wvm.update(week);

                Snackbar.make(requireView(),
                        resources.getString(R.string.snackbar_message_update_success, title),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        });
        dialog.setText(sfvm.getCurrentWeek().week.title);

        dialog.show(getParentFragmentManager(), "Week edition");
    }

    private void openCreationDialog() {
        new TextInputDialog(new TextInputDialog.Listener() {
            @Override
            public void onPositiveClick(DialogFragment dialog) {
                EditText et = Objects.requireNonNull(dialog.getDialog())
                        .findViewById(R.id.new_week_title);

                String title = et.getText().toString().trim();

                Week week = new Week(title);

                wvm.insert(week);

                Snackbar.make(requireView(),
                        resources.getString(R.string.snackbar_message_creation_success, title),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        }).show(getParentFragmentManager(), "Week creation");
    }

    private void openConfirmationDialog(ConfirmationScenario scenario) {
        switch (scenario) {
            case DeleteDay:
                setupDeleteDayDialog().show(getParentFragmentManager(), "Delete day confirmation");
                break;
            case DeleteWeek:
                setupDeleteWeekDialog().show(getParentFragmentManager(), "Delete week confirmation");
                break;
            default:
                break;
        }
    }

    private void openSelectionDialog(List<DaysOfWeek> availableDays) {
        new ListDialog(R.string.dialog_title_select_day, availableDays, (dialog, itemNumber) -> {
            dvm.insert(new Day(availableDays.get(itemNumber), sfvm.getCurrentWeek().week.id));
            availableDays.remove(itemNumber);
            wvm.update(sfvm.getCurrentWeek().week);
        })
                .show(getParentFragmentManager(), "Select day dialog");
    }

    private ConfirmationDialog setupDeleteDayDialog() {
        int titleId = R.string.dialog_title_delete_one;
        String title = sfvm.getDayToDeleteName();

        String message = resources.getString(R.string.dialog_message_delete_day, title);
        String snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Snackbar s = Snackbar.make(requireView(), snackbarMes, Snackbar.LENGTH_LONG);

                Day dayToDelete = sfvm.getDayToDelete();
                Class[] classes = cvm.findAllClassesForADayAsArray(dayToDelete.id);

                s.setAction(R.string.snackbar_action_undone, v -> restoreDeletedDay(dayToDelete, classes));

                deleteSelectedDay();

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

    private ConfirmationDialog setupDeleteWeekDialog() {
        int titleId = R.string.dialog_title_delete_one;
        String title = sfvm.getCurrentWeek().week.title;

        String message = resources.getString(R.string.dialog_message_delete_week, title);
        String snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Snackbar s = Snackbar.make(requireView(), snackbarMes, Snackbar.LENGTH_LONG);

                WeekWithDays weekToDelete = sfvm.getCurrentWeek();
                List<Class[]> classes = new LinkedList<>();
                for (Day day : weekToDelete.days) {
                    classes.add(cvm.findAllClassesForADayAsArray(day.id));
                }

                s.setAction(R.string.snackbar_action_undone, v ->
                        restoreDeletedWeek(weekToDelete, classes));

                wvm.delete(weekToDelete.week);

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

    private void restoreDeletedWeek(WeekWithDays weekToRestore, List<Class[]> classes) {
        wvm.insert(weekToRestore.week);
        dvm.insert(weekToRestore.days.toArray(new Day[0]));
        for (Class[] classArray : classes) {
            cvm.insert(classArray);
        }
    }

    private void deleteSelectedDay() {
        Day dayToDelete = sfvm.getDayToDelete();
        dvm.delete(dayToDelete);
        unregisterForContextMenu(tabToDelete);

        WeekWithDays currentWeek = sfvm.getCurrentWeek();

        currentWeek.week.availableDays.add(DaysOfWeek.fromInt(dayToDelete.order));
        Collections.sort(currentWeek.week.availableDays);
        wvm.update(currentWeek.week);
    }

    private void restoreDeletedDay(Day day, Class[] classes) {
        dvm.insert(day);
        cvm.insert(classes);
        WeekWithDays currentWeek = sfvm.getCurrentWeek();

        currentWeek.week.availableDays.remove(DaysOfWeek.fromInt(day.order));
        wvm.update(currentWeek.week);
    }

    private enum ConfirmationScenario {
        DeleteDay,
        DeleteWeek
    }
}