package com.vo1d.schedulemanager.v2.ui.lecturers.list;

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
import com.vo1d.schedulemanager.v2.data.lecturers.Lecturer;
import com.vo1d.schedulemanager.v2.data.lecturers.LecturerViewModel;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog.DELETE_SELECTED;

public class LecturersListFragment extends Fragment {

    private LecturersListFragmentDirections.EditLecturer edition;

    private LecturersListViewModel llvm;
    private LecturerViewModel lvm;
    private Resources resources;
    private LecturersListAdapter adapter;

    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private MaterialTextView listIsEmptyTextView;
    private MaterialTextView nothingFoundTextView;
    private ActionMode actionMode;
    private SelectionTracker<Long> tracker;
    private MenuItem deleteAll;

    private int visibility;
    private boolean inSearchMode = false;
    private boolean isDeleteAction = false;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
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
                    openConfirmationDialog(DELETE_SELECTED);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (!isDeleteAction) {
                tracker.clearSelection();
                llvm.clearSelection();
            }
            isDeleteAction = false;
            actionMode = null;
            fabAdd.show();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvm = new ViewModelProvider(requireActivity()).get(LecturerViewModel.class);
        resources = getResources();
        adapter = new LecturersListAdapter();

        return inflater.inflate(R.layout.fragment_lecturers_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        llvm = new ViewModelProvider(this).get(LecturersListViewModel.class);

        recyclerView = view.findViewById(R.id.lecturers_list);
        fabAdd = view.findViewById(R.id.add_lecturer_fab);
        listIsEmptyTextView = view.findViewById(R.id.lecturers_placeholder);
        nothingFoundTextView = view.findViewById(R.id.search_placeholder);

        edition = LecturersListFragmentDirections.editLecturer();

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
                Navigation.findNavController(v).navigate(R.id.add_lecturer));

        adapter.setOnSelectionChangedListener((lecturer, itemView, isChecked) -> {
            if (isChecked) {
                llvm.addToSelection(lecturer, itemView);
            } else {
                llvm.removeFromSelection(lecturer, itemView);
            }
        });

        adapter.setOnItemClickListener(lecturer -> {
                    if (actionMode == null) {
                        edition.setLecturerId(lecturer.id);
                        Navigation.findNavController(view).navigate(edition);
                    }
                }
        );

        lvm.getAllLecturers().observe(getViewLifecycleOwner(), adapter::submitList);

        lvm.getAllLecturers().observe(getViewLifecycleOwner(), subjects -> {
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
                new LecturerKeyProvider(adapter, recyclerView),
                new LecturerDetailsLookup(recyclerView),
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

        llvm.getSelectedItems().observe(getViewLifecycleOwner(), list -> {
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
                        LecturersListFragment.this::deleteAllItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

                List<Lecturer> backupList = adapter.getCurrentList();

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
            String title = Objects.requireNonNull(llvm.getSelectedItems().getValue()).get(0).getShortName();
            snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);
        } else if (count == Objects.requireNonNull(lvm.getAllLecturers().getValue()).size()) {
            snackbarMes = resources.getString(R.string.snackbar_message_all_deleted);
        } else {
            snackbarMes = resources.getString(R.string.snackbar_message_selected_deleted, count);
        }

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                actionMode.finish();

                Snackbar s = Snackbar.make(recyclerView, snackbarMes, 2750);

                ScheduledFuture<?> operation = executor.schedule(
                        LecturersListFragment.this::deleteSelectedItems,
                        2750,
                        TimeUnit.MILLISECONDS
                );

                llvm.getSelectedViews().forEach(view -> view.setVisibility(View.GONE));

                s.setAction(R.string.snackbar_action_undone, v -> {
                    operation.cancel(false);
                    llvm.getSelectedViews().forEach(view -> view.setVisibility(View.VISIBLE));
                    llvm.clearSelection();
                    tracker.clearSelection();
                });

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
        lvm.delete(llvm.getSelectedItemsAsArray(new Lecturer[0]));
        tracker.clearSelection();
    }

    private void deleteAllItems() {
        lvm.deleteAll();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subjects, menu);

        MenuItem item = menu.findItem(R.id.search_action);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                inSearchMode = true;
                lvm.getAllLecturers().removeObserver(adapter::submitList);
                fabAdd.hide();
                visibility = listIsEmptyTextView.getVisibility();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                inSearchMode = false;
                lvm.getAllLecturers().observe(getViewLifecycleOwner(), adapter::submitList);
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
                    adapter.submitList(lvm.getAllLecturers().getValue());
                    nothingFoundTextView.setVisibility(View.GONE);
                    listIsEmptyTextView.setVisibility(visibility);
                } else {
                    List<Lecturer> filteredList = lvm.getFilteredLecturers(newText);
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

        lvm.getAllLecturers().observe(this, subjects -> {
            if (subjects.size() == 0) {
                deleteAll.setEnabled(false);
            } else {
                deleteAll.setEnabled(true);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_action) {
            openConfirmationDialog(ConfirmationDialog.DELETE_ALL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}