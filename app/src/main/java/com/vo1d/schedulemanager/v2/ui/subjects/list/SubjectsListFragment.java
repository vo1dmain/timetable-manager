package com.vo1d.schedulemanager.v2.ui.subjects.list;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
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
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectViewModel;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog.DELETE_SELECTED;

public class SubjectsListFragment extends Fragment {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private SubjectViewModel svm;
    private SubjectsListViewModel slvm;
    private Resources resources;
    private SubjectsListAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private MaterialTextView listIsEmptyTextView;
    private MaterialTextView nothingFoundTextView;
    private MenuItem deleteAll;
    private SubjectsListFragmentDirections.EditSubject edition;
    private int visibility;
    private boolean inSearchMode = false;
    private boolean isDeleteAction = false;
    private ActionMode actionMode;
    private SelectionTracker<Long> tracker;
    private final ActionMode.Callback callback = new ActionMode.Callback() {
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
            int itemId = item.getItemId();
            if (itemId == R.id.select_all) {
                tracker.setItemsSelected(adapter.getAllIds(), true);
                return true;
            } else if (itemId == R.id.delete_selected) {
                isDeleteAction = true;
                openConfirmationDialog(DELETE_SELECTED);
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (!isDeleteAction) {
                tracker.clearSelection();
                slvm.clearSelection();
            }
            isDeleteAction = false;
            actionMode = null;
            fabAdd.show();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        svm = new ViewModelProvider(requireActivity()).get(SubjectViewModel.class);
        resources = getResources();
        adapter = new SubjectsListAdapter();

        return inflater.inflate(R.layout.fragment_subjects_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        slvm = new ViewModelProvider(this).get(SubjectsListViewModel.class);

        recyclerView = view.findViewById(R.id.subjects_list);
        fabAdd = view.findViewById(R.id.add_subject_fab);
        listIsEmptyTextView = view.findViewById(R.id.subjects_placeholder);
        nothingFoundTextView = view.findViewById(R.id.search_placeholder);

        edition = SubjectsListFragmentDirections.editSubject();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        recyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!inSearchMode) {
                if (oldScrollY < scrollY) {
                    fabAdd.hide();
                } else {
                    fabAdd.show();
                }
            }
        });

        fabAdd.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.add_subject));

        adapter.setOnSelectionChangedListener((subject, isChecked) -> {
            if (isChecked) {
                slvm.addToSelection(subject);
            } else {
                slvm.removeFromSelection(subject);
            }
        });

        adapter.setOnItemClickListener(subject -> {
                    if (actionMode == null) {
                        edition.setSubjectId(subject.id);
                        Navigation.findNavController(view).navigate(edition);
                    }
                }
        );

        svm.getAllSubjects().observe(getViewLifecycleOwner(), adapter::submitList);

        svm.getAllSubjects().observe(getViewLifecycleOwner(), subjects -> {
            if (subjects.size() == 0) {
                if (actionMode != null) {
                    actionMode.finish();
                }
                recyclerView.setVisibility(View.GONE);
                listIsEmptyTextView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                listIsEmptyTextView.setVisibility(View.GONE);
            }
        });

        tracker = new SelectionTracker.Builder<>(
                "selectedSubjectTrackerId",
                recyclerView,
                new SubjectKeyProvider(adapter, recyclerView),
                new SubjectDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        )
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
                if (tracker.hasSelection()) {
                    if (actionMode == null) {
                        actionMode = ((MainActivity) requireActivity()).startSupportActionMode(callback);
                    } else {
                        actionMode.setTitle(resources.getString(R.string.selection_count, tracker.getSelection().size()));
                    }
                } else if (!tracker.hasSelection()) {
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                }
            }
        });

        adapter.setTracker(tracker);

        slvm.getSelectedItems().observe(getViewLifecycleOwner(), list -> {
            if (list.isEmpty()) {
                tracker.clearSelection();
            }
        });
    }

    private void openConfirmationDialog(int confirmationType) {
        if (confirmationType == ConfirmationDialog.DELETE_ALL) {
            setupDeleteAllDialog().show(getParentFragmentManager(), "Delete all confirmation");
        } else if (confirmationType == DELETE_SELECTED) {
            setupDeleteSelectedDialog().show(getParentFragmentManager(), "Delete selected confirmation");
        }
    }

    private ConfirmationDialog setupDeleteAllDialog() {
        int titleId = R.string.dialog_title_delete_multiple_items;
        String message = resources.getString(R.string.dialog_message_delete_all);

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Snackbar s = Snackbar.make(recyclerView, R.string.snackbar_message_all_deleted, 2750);

                ScheduledFuture<?> operation = executor.schedule(
                        SubjectsListFragment.this::deleteAllItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

                List<Subject> backupList = adapter.getCurrentList();

                s.setAction(R.string.snackbar_action_undone, v -> {
                    operation.cancel(false);
                    adapter.submitList(backupList);
                });

                adapter.clearData();

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

    private ConfirmationDialog setupDeleteSelectedDialog() {
        int count = tracker.getSelection().size();

        int titleId = R.string.dialog_title_delete_multiple_items;
        String message = resources.getString(R.string.dialog_message_delete_selected, count);

        String snackbarMes;
        if (count == 1) {
            String title = Objects.requireNonNull(slvm.getSelectedItems().getValue()).get(0).title;
            snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);
        } else if (count == Objects.requireNonNull(svm.getAllSubjects().getValue()).size()) {
            snackbarMes = resources.getString(R.string.snackbar_message_all_deleted);
        } else {
            snackbarMes = resources.getString(R.string.snackbar_message_selected_deleted, count);
        }

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                actionMode.finish();

                Snackbar s = Snackbar.make(recyclerView, snackbarMes, 2750);

                List<Subject> data = slvm.getSelectedItems().getValue();

                List<Subject> backedUpData = adapter.getCurrentList();

                ScheduledFuture<?> operation = executor.schedule(
                        SubjectsListFragment.this::deleteSelectedItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

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

        ConfirmationDialog dialog = new ConfirmationDialog(titleId, message);
        dialog.setDialogListener(listener);

        return dialog;
    }

    private void deleteSelectedItems() {
        svm.delete(slvm.getSelectedItemsAsArray(new Subject[0]));
        tracker.clearSelection();
    }

    private void deleteAllItems() {
        svm.deleteAll();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subjects, menu);

        MenuItem item = menu.findItem(R.id.search_action);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                inSearchMode = true;
                svm.getAllSubjects().removeObserver(adapter::submitList);
                fabAdd.hide();
                visibility = listIsEmptyTextView.getVisibility();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                inSearchMode = false;
                svm.getAllSubjects().observe(getViewLifecycleOwner(), adapter::submitList);
                fabAdd.show();
                nothingFoundTextView.setVisibility(View.GONE);
                return true;
            }
        });

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    adapter.submitList(svm.getAllSubjects().getValue());
                    nothingFoundTextView.setVisibility(View.GONE);
                    listIsEmptyTextView.setVisibility(visibility);
                } else {
                    List<Subject> filteredList = svm.getFilteredSubjects(newText);
                    adapter.submitList(filteredList);
                    listIsEmptyTextView.setVisibility(View.GONE);
                    if (filteredList.size() == 0) {
                        nothingFoundTextView.setVisibility(View.VISIBLE);
                    } else {
                        nothingFoundTextView.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });

        deleteAll = menu.findItem(R.id.delete_all_action);

        svm.getAllSubjects().observe(this, subjects -> deleteAll.setEnabled(subjects.size() != 0));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all_action) {
            openConfirmationDialog(ConfirmationDialog.DELETE_ALL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}