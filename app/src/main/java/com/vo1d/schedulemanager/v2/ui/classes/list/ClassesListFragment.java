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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
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
    private MaterialTextView listIsEmptyTextView;
    private ScheduleFragmentDirections.AddClass addClass;
    private ScheduleFragmentDirections.EditClass editClass;
    private MaterialButton buttonAdd;
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(resources.getString(R.string.selection_count, 0));
            buttonAdd.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.select_all) {
                adapter.selectAll();
                return true;
            } else if (itemId == R.id.delete_selected) {
                isDeleteAction = true;
                openConfirmationDialog();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (!isDeleteAction) {
                clvm.clearSelection();
            }
            isDeleteAction = false;
            setActionMode(null);
            finishEditionMode();
            buttonAdd.setVisibility(View.GONE);
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
        listIsEmptyTextView = view.findViewById(R.id.classes_placeholder);
        buttonAdd = view.findViewById(R.id.add_class_button);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        buttonAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(addClass);
            ActionMode am = getActionMode();
            if (am != null) {
                am.finish();
            }
        });

        adapter.setSelectionChangedListener((c, isChecked) -> {
            if (isChecked) {
                clvm.addToSelection(c);
            } else {
                clvm.removeFromSelection(c);
            }
        });

        adapter.setItemButtonClickListener(c -> {
            editClass.setClassId(c.aClass.id);
            Navigation.findNavController(requireView()).navigate(editClass);
            ActionMode am = getActionMode();
            if (am != null) {
                am.finish();
            }
        });

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

        clvm.getSelectedItems().observe(getViewLifecycleOwner(), list -> {
            ActionMode am = getActionMode();
            if (am != null) {
                am.setTitle(resources.getString(R.string.selection_count, list.size()));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("dayId", dayId);
    }

    public void startEditionMode() {
        setActionMode(((MainActivity) requireActivity()).startSupportActionMode(callback));
        adapter.setEditionMode(true);
    }

    public void finishEditionMode() {
        adapter.setEditionMode(false);
    }

    private void openConfirmationDialog() {
        ConfirmationDialog confirmationDialog;
        ConfirmationDialog.DialogListener listener;

        int count = Objects.requireNonNull(clvm.getSelectedItems().getValue()).size();

        int titleId = R.string.dialog_title_delete_multiple_items;
        String message = resources.getString(R.string.dialog_message_delete_selected, count);

        String snackbarMes;
        if (count == 1) {
            Subject s = Objects.requireNonNull(clvm.getSelectedItems().getValue()).get(0).subject;
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

                Snackbar s = Snackbar.make(recyclerView, snackbarMes, 2750);

                List<ClassWithSubject> data = clvm.getSelectedItems().getValue();
                Class[] classes = clvm.getSelectedItemsAsClassArray(new Class[0]);

                ScheduledFuture<?> operation = executor.schedule(
                        () -> deleteSelectedItems(classes),
                        2750,
                        TimeUnit.MILLISECONDS
                );

                List<ClassWithSubject> backedUpData = adapter.getCurrentList();

                s.setAction(R.string.snackbar_action_undone, v -> {
                    operation.cancel(false);
                    adapter.submitList(backedUpData);
                    clvm.clearSelection();
                });

                adapter.removeData(data);

                getActionMode().finish();

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

    private void deleteSelectedItems(Class[] classes) {
        cvm.delete(classes);
        clvm.clearSelection();
    }
}
