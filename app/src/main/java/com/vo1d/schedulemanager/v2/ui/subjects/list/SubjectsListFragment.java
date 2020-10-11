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
import com.vo1d.schedulemanager.v2.data.subject.Subject;
import com.vo1d.schedulemanager.v2.data.subject.SubjectsViewModel;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;

import java.util.List;
import java.util.Objects;

import static com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog.DEFAULT_LISTENER;
import static com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog.DELETE_SELECTED;

public class SubjectsListFragment extends Fragment {

    private SubjectsViewModel svm;
    private Resources resources;
    private SubjectsListAdapter adapter;

    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private MaterialTextView listIsEmptyTextView;
    private MaterialTextView nothingFoundTextView;
    private ActionMode actionMode;
    private SelectionTracker<Long> tracker;
    private MenuItem deleteAll;

    private SubjectsListFragmentDirections.EditSubject edition;
    private int visibility;
    private boolean inSearchMode = false;

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
                    openConfirmationDialog(DELETE_SELECTED);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            tracker.clearSelection();
            svm.clearSelection();
            actionMode = null;
            fabAdd.show();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        svm = new ViewModelProvider(requireActivity()).get(SubjectsViewModel.class);
        resources = getResources();
        adapter = new SubjectsListAdapter();

        return inflater.inflate(R.layout.fragment_subjects_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

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
                svm.addToSelection(subject);
            } else {
                svm.removeFromSelection(subject);
            }
        });

        adapter.setOnItemClickListener(subject -> {
                    edition.setSubjectId(subject.getId());
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                    Navigation.findNavController(view).navigate(edition);
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

        svm.getSelectedSubjects().observe(getViewLifecycleOwner(), list -> {
            if (list.isEmpty()) {
                tracker.clearSelection();
            }
        });
    }

    private void openConfirmationDialog(int confirmationType) {
        int titleId = R.string.dialog_title_placeholder;
        String message = "";
        ConfirmationDialog confirmationDialog;
        ConfirmationDialog.DialogListener listener = DEFAULT_LISTENER;

        if (confirmationType == ConfirmationDialog.DELETE_ALL) {
            titleId = R.string.dialog_title_delete_multiple_items;
            message = resources.getString(R.string.dialog_message_delete_all);

            listener = new ConfirmationDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog) {
                    svm.deleteAllSubjects();
                    Snackbar.make(recyclerView, R.string.snackbar_message_all_deleted, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {
                    dialog.dismiss();
                }
            };
        } else if (confirmationType == DELETE_SELECTED) {
            int count = tracker.getSelection().size();

            titleId = R.string.dialog_title_delete_multiple_items;
            message = resources.getString(R.string.dialog_message_delete_selected, count);

            String snackbarMes;
            if (count == 1) {
                String title = Objects.requireNonNull(svm.getSelectedSubjects().getValue()).get(0).getTitle();
                snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);
            } else if (count == Objects.requireNonNull(svm.getAllSubjects().getValue()).size()) {
                snackbarMes = resources.getString(R.string.snackbar_message_all_deleted);
            } else {
                snackbarMes = resources.getString(R.string.snackbar_message_selected_deleted, count);
            }
            listener = new ConfirmationDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog) {
                    svm.deleteSelectedSubjects();
                    actionMode.finish();
                    Snackbar.make(recyclerView, snackbarMes, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {
                    dialog.dismiss();
                }
            };
        }

        confirmationDialog = new ConfirmationDialog(titleId, message);
        confirmationDialog.setDialogListener(listener);
        confirmationDialog.show(getParentFragmentManager(), "Delete selected confirmation");
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

        svm.getAllSubjects().observe(this, subjects -> {
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