package com.vo1d.schedulemanager.v2.ui.courses.list;

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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.vo1d.schedulemanager.v2.MainActivity;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.courses.CourseViewModel;
import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;
import com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog;

import java.util.List;
import java.util.Objects;

import static com.vo1d.schedulemanager.v2.MainActivity.getActionMode;
import static com.vo1d.schedulemanager.v2.MainActivity.setActionMode;
import static com.vo1d.schedulemanager.v2.ui.dialogs.ConfirmationDialog.DELETE_SELECTED;

public class CoursesListFragment extends Fragment {

    private final ActionMode.Callback callback;

    private CoursesListAdapter adapter;

    private CoursesListFragmentDirections.EditCourse edition;

    private CoursesListViewModel listViewModel;
    private CourseViewModel courseViewModel;

    private MaterialTextView listIsEmptyTextView;
    private MaterialTextView nothingFoundTextView;

    private MenuItem deleteAll;

    private RecyclerView recyclerView;
    private Resources resources;

    private SelectionTracker<Long> tracker;
    private int visibility;

    public CoursesListFragment() {
        callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(resources.getString(R.string.selection_count, tracker.getSelection().size()));
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.select_all) {
                    tracker.setItemsSelected(adapter.getAllIds(), true);
                    return true;
                } else if (itemId == R.id.delete_selected) {
                    openConfirmationDialog(DELETE_SELECTED);
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                tracker.clearSelection();
                listViewModel.clearSelection();
                setActionMode(null);
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        resources = getResources();
        adapter = new CoursesListAdapter();

        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewModel = new ViewModelProvider(this).get(CoursesListViewModel.class);

        recyclerView = view.findViewById(R.id.courses_list);
        listIsEmptyTextView = view.findViewById(R.id.courses_placeholder);
        nothingFoundTextView = view.findViewById(R.id.search_placeholder);

        edition = CoursesListFragmentDirections.editCourse();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        adapter.setOnSelectionChangedListener((course, isChecked) -> {
            if (isChecked) {
                listViewModel.addToSelection(course);
            } else {
                listViewModel.removeFromSelection(course);
            }
        });

        adapter.setOnItemClickListener(course -> {
            if (getActionMode() == null) {
                edition.setCourseId(course.course.id);
                Navigation.findNavController(view).navigate(edition);
            }
        });

        courseViewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);

        courseViewModel.getAll().observe(getViewLifecycleOwner(), courses -> {
            if (courses.size() == 0) {
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
                "selectedCourseTrackerId",
                recyclerView,
                new CourseKeyProvider(adapter, recyclerView),
                new CourseDetailsLookup(recyclerView),
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

        listViewModel.getSelectedItems().observe(getViewLifecycleOwner(), list -> {
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
            public void onPositiveClick() {
                Snackbar s = Snackbar.make(recyclerView, R.string.snackbar_message_all_deleted, 2750);

                CourseWithInstructors[] backupArray = adapter.getCurrentList().toArray(new CourseWithInstructors[0]);

                s.setAction(R.string.snackbar_action_undone, v -> restoreDeletedCourses(backupArray));

                deleteAllItems();

                s.show();
            }

            @Override
            public void onNegativeClick(DialogFragment dialog) {
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
            String title = Objects.requireNonNull(listViewModel.getSelectedItems().getValue()).get(0).course.title;
            snackbarMes = resources.getString(R.string.snackbar_message_delete_success, title);
        } else if (count == Objects.requireNonNull(courseViewModel.getAll().getValue()).size()) {
            snackbarMes = resources.getString(R.string.snackbar_message_all_deleted);
        } else {
            snackbarMes = resources.getString(R.string.snackbar_message_selected_deleted, count);
        }

        ConfirmationDialog.DialogListener listener = new ConfirmationDialog.DialogListener() {
            @Override
            public void onPositiveClick() {
                Snackbar s = Snackbar.make(recyclerView, snackbarMes, 2750);

                CourseWithInstructors[] array = listViewModel.getSelectedItemsAsArray(new CourseWithInstructors[0]);

                s.setAction(R.string.snackbar_action_undone, v -> restoreDeletedCourses(array));

                getActionMode().finish();

                deleteSelectedItems(array);

                s.show();
            }

            @Override
            public void onNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        };

        ConfirmationDialog dialog = new ConfirmationDialog(titleId, message);
        dialog.setDialogListener(listener);

        return dialog;
    }

    private void restoreDeletedCourses(CourseWithInstructors[] data) {
        for (CourseWithInstructors c :
                data) {
            courseViewModel.insert(c.course);
            c.junctions.forEach(junction -> courseViewModel.insertJunctions(junction));
        }
    }

    private void deleteSelectedItems(CourseWithInstructors[] array) {
        for (CourseWithInstructors c :
                array) {
            courseViewModel.delete(c.course);
        }
        tracker.clearSelection();
    }

    private void deleteAllItems() {
        courseViewModel.deleteAll();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_courses, menu);

        MenuItem item = menu.findItem(R.id.search_action);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                courseViewModel.getAll().removeObserver(adapter::submitList);
                visibility = listIsEmptyTextView.getVisibility();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                courseViewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);
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
                    adapter.submitList(courseViewModel.getAll().getValue());
                    nothingFoundTextView.setVisibility(View.GONE);
                    listIsEmptyTextView.setVisibility(visibility);
                } else {
                    List<CourseWithInstructors> filteredList = courseViewModel.getFiltered(newText);
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

        courseViewModel.getAll().observe(this, courses -> deleteAll.setEnabled(courses.size() != 0));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all_action) {
            openConfirmationDialog(ConfirmationDialog.DELETE_ALL);
            return true;
        } else if (id == R.id.add_day_action) {
            Navigation.findNavController(requireView()).navigate(R.id.add_course);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}