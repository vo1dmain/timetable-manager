package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.vo1d.schedulemanager.v2.MainActivity;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassViewModel;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithSubject;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;
import com.vo1d.schedulemanager.v2.ui.schedule.ScheduleFragmentDirections;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.vo1d.schedulemanager.v2.ui.schedule.ScheduleFragment.getActionMode;
import static com.vo1d.schedulemanager.v2.ui.schedule.ScheduleFragment.setActionMode;

public class ClassesListFragment extends Fragment {


    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private boolean isDeleteAction = false;
    private int dayId;

    private ClassViewModel cvm;
    private ClassesListViewModel clvm;
    private Resources resources;
    private ClassesListAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private MaterialTextView listIsEmptyTextView;
    private SelectionTracker<Long> tracker;
    private ScheduleFragmentDirections.AddClass addClass;
    private ScheduleFragmentDirections.EditClass editClass;
    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(resources.getString(R.string.selection_count, tracker.getSelection().size()));
            fabAdd.hide();
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.select_all:
                    tracker.setItemsSelected(adapter.getAllIds(), true);
                    return true;
                case R.id.delete_selected:
                    isDeleteAction = true;
                    openConfirmationDialog();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (!isDeleteAction) {
                tracker.clearSelection();
                clvm.clearSelection();
            }
            isDeleteAction = false;
            setActionMode(null);
            fabAdd.show();
        }
    };

    public ClassesListFragment() {
        super();
    }

    public ClassesListFragment(int dayId) {
        super();
        addClass = ScheduleFragmentDirections.addClass(dayId);
        editClass = ScheduleFragmentDirections.editClass();

        this.dayId = dayId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            dayId = savedInstanceState.getInt("dayId");
        }

        clvm = new ViewModelProvider(this).get(ClassesListViewModel.class);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        cvm = provider.get(ClassViewModel.class);

        clvm.setAllClassesForADay2(cvm.findAllClassesForADay2(dayId));

        resources = getResources();
        adapter = new ClassesListAdapter(resources);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.classes_list);
        fabAdd = view.findViewById(R.id.add_class_fab);
        listIsEmptyTextView = view.findViewById(R.id.classes_placeholder);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        recyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (oldScrollY < scrollY) {
                fabAdd.hide();
            } else {
                fabAdd.show();
            }
        });

        fabAdd.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(addClass));

        adapter.setOnSelectionChangedListener((c, isChecked) -> {
            if (isChecked) {
                clvm.addToSelection(c);
            } else {
                clvm.removeFromSelection(c);
            }
        });

        adapter.setOnItemClickListener(c -> {
                    if (getActionMode() == null) {
                        editClass.setClassId(c.aClass.id);
                        Navigation.findNavController(view).navigate(editClass);
                    }
                }
        );

        clvm.getAllClassesForADay2().observe(getViewLifecycleOwner(), adapter::submitList);

        clvm.getAllClassesForADay2().observe(getViewLifecycleOwner(), classes -> {
            if (classes.size() == 0) {
                if (getActionMode() != null) {
                    getActionMode().finish();
                }
                recyclerView.setVisibility(View.GONE);
                listIsEmptyTextView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                listIsEmptyTextView.setVisibility(View.GONE);
            }
        });

        tracker = new SelectionTracker.Builder<>(
                "selectedClassTrackerId",
                recyclerView,
                new ClassKeyProvider(adapter, recyclerView),
                new ClassDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        )
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
                if (tracker.hasSelection()) {
                    if (getActionMode() == null) {
                        setActionMode(((MainActivity) requireActivity()).startSupportActionMode(callback));
                    } else {
                        getActionMode().setTitle(resources.getString(R.string.selection_count, tracker.getSelection().size()));
                    }
                } else if (!tracker.hasSelection()) {
                    if (getActionMode() != null) {
                        getActionMode().finish();
                    }
                }
            }
        });

        adapter.setTracker(tracker);

        clvm.getSelectedItems().observe(getViewLifecycleOwner(), list -> {
            if (list.isEmpty()) {
                tracker.clearSelection();
            }
        });
    }

    private void openConfirmationDialog() {
        ConfirmationDialog confirmationDialog;
        ConfirmationDialog.DialogListener listener;

        int count = tracker.getSelection().size();

        int titleId = R.string.dialog_title_delete_multiple_items;
        String message = resources.getString(R.string.dialog_message_delete_selected, count);

        String snackbarMes;
        if (count == 1) {
            Subject s = Objects.requireNonNull(clvm.getAllClassesForADay2().getValue()).get(0).subject;
            String title = s.title;
            snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);
        } else if (count == Objects.requireNonNull(clvm.getAllClassesForADay2().getValue()).size()) {
            snackbarMes = resources.getString(R.string.snackbar_message_all_deleted);
        } else {
            snackbarMes = resources.getString(R.string.snackbar_message_selected_deleted, count);
        }

        listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                getActionMode().finish();

                Snackbar s = Snackbar.make(recyclerView, snackbarMes, 2750);

                ScheduledFuture<?> operation = executor.schedule(
                        ClassesListFragment.this::deleteSelectedItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

                List<ClassWithSubject> data = clvm.getSelectedItems().getValue();

                List<ClassWithSubject> backedUpData = adapter.getCurrentList();

                s.setAction(R.string.snackbar_action_undone, v -> {
                    operation.cancel(false);
                    adapter.submitList(backedUpData);
                    tracker.clearSelection();
                });

                adapter.removeData(data);

                s.show();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        };

        confirmationDialog = new ConfirmationDialog(titleId, message);
        confirmationDialog.setDialogListener(listener);
        confirmationDialog.show(getParentFragmentManager(), "Delete selected confirmation");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("dayId", dayId);
    }

    private void deleteSelectedItems() {
        cvm.delete(clvm.getSelectedItemsAsClassArray(new Class[0]));
        tracker.clearSelection();
    }
}
