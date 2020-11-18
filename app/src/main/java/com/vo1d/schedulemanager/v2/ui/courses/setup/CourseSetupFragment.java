package com.vo1d.schedulemanager.v2.ui.courses.setup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;
import com.vo1d.schedulemanager.v2.data.courses.Course;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.courses.CourseViewModel;
import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorMinimised;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorViewModel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CourseSetupFragment extends Fragment {

    private CourseViewModel cvm;
    private CourseSetupViewModel csvm;
    private TextInputEditText titleInput;
    private MultiAutoCompleteTextView instructorInput;

    private boolean isEditionMode = false;
    private CourseWithInstructors current;
    private ChipGroup instructorsChips;
    private ArrayAdapter<InstructorMinimised> arrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(this);
        csvm = provider.get(CourseSetupViewModel.class);
        cvm = provider.get(CourseViewModel.class);
        InstructorViewModel lvm = provider.get(InstructorViewModel.class);

        csvm.setupInstructors(lvm.getAllMinimisedAsList());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_course_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instructorsChips = view.findViewById(R.id.selected_instructors);
        titleInput = view.findViewById(R.id.course_title_input);
        instructorInput = view.findViewById(R.id.instructors_setup);
        ChipGroup types = view.findViewById(R.id.types_list);

        List<Integer> chipIds = new LinkedList<>(Collections.emptyList());

        for (CourseTypes t : CourseTypes.values()) {
            Chip c = (Chip) LayoutInflater.from(requireContext())
                    .inflate(R.layout.chip_course_type_choice, types, false);
            c.setText(t.toString());
            c.setTag(t);
            c.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    csvm.addTypeToSelection((CourseTypes) buttonView.getTag());
                } else {
                    csvm.removeTypeFromSelection((CourseTypes) buttonView.getTag());
                }
            });
            types.addView(c);

            chipIds.add(c.getId());
        }

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                csvm.titleIsFilled.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        arrayAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                csvm.getAllInstructors()
        );

        instructorInput.setAdapter(arrayAdapter);

        instructorInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        instructorInput.setOnItemClickListener((parent, view1, position, id) -> {
            InstructorMinimised selected = (InstructorMinimised) parent.getItemAtPosition(position);
            addInstructor(selected);
        });

        csvm.getSelectedInstructors().observe(getViewLifecycleOwner(), instructors -> {
            instructorsChips.removeAllViewsInLayout();
            for (InstructorMinimised instructor :
                    instructors) {
                Chip chip = (Chip) LayoutInflater.from(requireContext())
                        .inflate(R.layout.chip_instructor_entry, instructorsChips, false);
                chip.setText(instructor.getShortName());
                chip.setTag(instructor);
                chip.setOnCloseIconClickListener(v -> {
                    arrayAdapter.add((InstructorMinimised) chip.getTag());
                    csvm.removeInstructorFromSelection((InstructorMinimised) chip.getTag());
                });
                instructorsChips.addView(chip);
            }
        });

        if (getArguments() != null) {
            int id = getArguments().getInt("courseId");
            isEditionMode = id != -1;
            if (isEditionMode) {
                current = cvm.findCourseById(id);
                titleInput.setText(current.course.title);
                current.instructors.forEach(instructor -> csvm.addInstructorToSelection(instructor));
                for (CourseTypes t :
                        current.course.getCourseTypes()) {
                    types.check(chipIds.get(t.ordinal()));
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        requireActivity().getMenuInflater().inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        csvm.canBeSaved.observe(getViewLifecycleOwner(), confirm::setEnabled);

        confirm.setOnMenuItemClickListener(item -> {
            if (isEditionMode) {
                applyCourseChanges();
            } else {
                createNewCourse();
            }

            InputMethodManager imm = requireContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            }

            return Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void addInstructor(InstructorMinimised selected) {
        csvm.addInstructorToSelection(selected);
        arrayAdapter.remove(selected);
        instructorInput.setText("");
    }

    private void createNewCourse() {
        String title = Objects.requireNonNull(titleInput.getText()).toString();
        Course newCourse = new Course(title, csvm.getSelectedTypesAsArray());

        int id = (int) cvm.insert(newCourse);

        List<CourseInstructor> junctions = new LinkedList<>();

        Objects.requireNonNull(csvm.getSelectedInstructors().getValue())
                .forEach(instructor -> junctions.add(new CourseInstructor(id, instructor.id))
                );

        cvm.insertJunctions(junctions.toArray(new CourseInstructor[0]));
    }

    private void applyCourseChanges() {
        current.course.title = Objects.requireNonNull(titleInput.getText()).toString();
        current.course.setCourseTypes(csvm.getSelectedTypesAsArray());

        List<CourseInstructor> junctions = new LinkedList<>();

        Objects.requireNonNull(csvm.getSelectedInstructors().getValue())
                .forEach(instructor -> junctions.add(new CourseInstructor(current.course.id, instructor.id))
                );

        cvm.update(current.course);
        cvm.deleteJunctions(current.junctions.toArray(new CourseInstructor[0]));
        cvm.insertJunctions(junctions.toArray(new CourseInstructor[0]));
    }
}
