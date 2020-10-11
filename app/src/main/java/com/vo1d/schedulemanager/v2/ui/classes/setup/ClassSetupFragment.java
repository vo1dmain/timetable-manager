package com.vo1d.schedulemanager.v2.ui.classes.setup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithSubject;
import com.vo1d.schedulemanager.v2.data.classes.ClassesViewModel;
import com.vo1d.schedulemanager.v2.data.subject.Subject;
import com.vo1d.schedulemanager.v2.data.subject.SubjectTypes;
import com.vo1d.schedulemanager.v2.data.subject.SubjectsViewModel;

import java.util.Collections;

@SuppressWarnings("FieldCanBeLocal")
public class ClassSetupFragment extends Fragment {
    private SubjectsViewModel svm;
    private ClassSetupViewModel csvm;
    private ClassesViewModel cvm;

    private ChipGroup typesChipGroup;
    private AppCompatSpinner subjectSpinner;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private TextInputEditText buildingInput;
    private TextInputEditText cabinetInput;

    private ClassWithSubject current;
    private boolean isEditionMode = false;
    private int id = -1;
    private ArrayAdapter<Subject> subjectsArrayAdapter;
    private Subject currentSubject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(this);

        csvm = provider.get(ClassSetupViewModel.class);
        cvm = provider.get(ClassesViewModel.class);

        svm = new ViewModelProvider(requireActivity()).get(SubjectsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_class_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectsArrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                csvm.getSubjectsList()
        );

        subjectSpinner = view.findViewById(R.id.subject_spinner);
        typesChipGroup = view.findViewById(R.id.types_list);

        startTimePicker = view.findViewById(R.id.start_time_picker);
        endTimePicker = view.findViewById(R.id.end_time_picker);
        buildingInput = view.findViewById(R.id.audience_building_input);
        cabinetInput = view.findViewById(R.id.audience_cabinet_input);

        startTimePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        endTimePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                if (obj != null) {
                    if (obj instanceof Subject) {
                        csvm.setSubjectId(((Subject) obj).getId());

                        typesChipGroup.clearCheck();
                        typesChipGroup.removeAllViewsInLayout();

                        if (((Subject) obj).getSubjectTypes().length == 1) {
                            SubjectTypes t = ((Subject) obj).getSubjectTypes()[0];
                            Chip c = (Chip) LayoutInflater.from(requireContext())
                                    .inflate(R.layout.chip_subject_type_choice, typesChipGroup, false);
                            c.setText(t.toString());
                            c.setClickable(false);
                            c.setCheckable(true);
                            c.setTag(t);
                            typesChipGroup.addView(c);
                            typesChipGroup.check(c.getId());
                        } else {
                            for (SubjectTypes t :
                                    ((Subject) obj).getSubjectTypes()) {
                                Chip c = (Chip) LayoutInflater.from(requireContext())
                                        .inflate(R.layout.chip_subject_type_choice, typesChipGroup, false);
                                c.setText(t.toString());
                                c.setClickable(true);
                                c.setCheckable(true);
                                c.setTag(t);
                                typesChipGroup.addView(c);
                            }
                        }

                        if (isEditionMode) {
                            Chip c = typesChipGroup.findViewWithTag(current.aClass.getType()[0]);
                            if (c != null) {
                                typesChipGroup.check(c.getId());
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                csvm.setSubjectId(-1);
                csvm.setType(null);
            }
        });

        typesChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip c = group.findViewById(checkedId);
            if (c != null) {
                csvm.setType((SubjectTypes) c.getTag());
            } else {
                csvm.setType(null);
            }
        });

        buildingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                csvm.setBuildingNumber(s.toString());
            }
        });

        cabinetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                csvm.setCabinetNumber(s.toString());
            }
        });

        subjectsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subjectSpinner.setAdapter(subjectsArrayAdapter);

        svm.getAllSubjects().observe(getViewLifecycleOwner(), subjects -> {
            csvm.getSubjectsList().addAll(subjects == null ? Collections.emptyList() : subjects);
            subjectsArrayAdapter.notifyDataSetChanged();
        });

        startTimePicker.setOnTimeChangedListener(
                (view1, hourOfDay, minute) -> endTimePicker.setHour(startTimePicker.getHour() + 1)
        );

        endTimePicker.setHour(startTimePicker.getHour() + 1);
        endTimePicker.setMinute(startTimePicker.getMinute() + 30);

        if (getArguments() != null) {
            csvm.setDayId(getArguments().getInt("dayId"));
            id = getArguments().getInt("classId");

            isEditionMode = id > 0;
            if (isEditionMode) {
                current = cvm.findClassById2(id);
                currentSubject = current.subject;

                buildingInput.setText(String.valueOf(current.aClass.audienceBuilding));
                cabinetInput.setText(String.valueOf(current.aClass.audienceCabinet));

                startTimePicker.setHour(current.aClass.getStartTimeHour());
                startTimePicker.setMinute(current.aClass.getStartTimeMinutes());
                endTimePicker.setHour(current.aClass.getEndTimeHour());
                endTimePicker.setMinute(current.aClass.getEndTimeMinutes());
                subjectSpinner.setSelection(subjectsArrayAdapter.getPosition(currentSubject));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        csvm.canBeSaved().observe(getViewLifecycleOwner(), confirm::setEnabled);

        confirm.setOnMenuItemClickListener(item -> {
            if (isEditionMode) {
                updateClass();
            } else {
                addNewClass();
            }

            InputMethodManager imm = requireContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            }

            return Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void updateClass() {
        current.aClass.subjectId = csvm.getSubjectId();
        current.aClass.setType(csvm.getType());
        current.aClass.audienceBuilding = csvm.getBuildingNumber();
        current.aClass.audienceCabinet = csvm.getCabinetNumber();
        current.aClass.setStartTimeHour(startTimePicker.getHour());
        current.aClass.setStartTimeMinutes(startTimePicker.getMinute());
        current.aClass.setEndTimeHour(endTimePicker.getHour());
        current.aClass.setEndTimeMinutes(endTimePicker.getMinute());

        cvm.update(current.aClass);
    }

    private void addNewClass() {
        Class newClass = new Class(
                csvm.getSubjectId(),
                csvm.getDayId(),
                csvm.getBuildingNumber(),
                csvm.getCabinetNumber(),
                csvm.getType()
        );
        newClass.setStartTimeHour(startTimePicker.getHour());
        newClass.setStartTimeMinutes(startTimePicker.getMinute());
        newClass.setEndTimeHour(endTimePicker.getHour());
        newClass.setEndTimeMinutes(endTimePicker.getMinute());

        cvm.insert(newClass);
    }
}
