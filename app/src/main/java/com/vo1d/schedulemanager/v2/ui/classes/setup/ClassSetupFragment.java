package com.vo1d.schedulemanager.v2.ui.classes.setup;

import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.vo1d.schedulemanager.v2.MainActivity;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassViewModel;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithCourse;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.courses.CourseViewModel;
import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorMinimised;
import com.vo1d.schedulemanager.v2.ui.TimeFormats;
import com.vo1d.schedulemanager.v2.ui.settings.SettingsFragment;

import java.sql.Time;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class ClassSetupFragment extends Fragment {

    private AppCompatSpinner courseSpinner;
    private ArrayAdapter<CourseWithInstructors> coursesArrayAdapter;

    private ChipGroup instructorsChipGroup;
    private ChipGroup typesChipGroup;
    private ClassSetupViewModel classSetupViewModel;
    private ClassViewModel classViewModel;
    private ClassWithCourse currentClass;
    private CourseViewModel courseViewModel;
    private CourseWithInstructors currentCourse;

    private DateFormat defaultFormatter;
    private SharedPreferences preferences;

    private TextInputEditText buildingInput;
    private TextInputEditText cabinetInput;
    private TextInputEditText startTimeInput;
    private TextInputEditText endTimeInput;

    private boolean isEditionMode = false;
    private int id = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(this);

        classSetupViewModel = provider.get(ClassSetupViewModel.class);
        classViewModel = provider.get(ClassViewModel.class);

        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(
                requireActivity().getApplicationContext()
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_class_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defaultFormatter = DateFormat.getTimeInstance(Calendar.getInstance(), DateFormat.SHORT);

        coursesArrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                classSetupViewModel.getCoursesList()
        );

        courseViewModel.getAll().observe(getViewLifecycleOwner(), courses -> {
            coursesArrayAdapter.addAll(courses == null ? Collections.emptyList() : courses);
            coursesArrayAdapter.notifyDataSetChanged();
            if (isEditionMode) {
                courseSpinner.setSelection(coursesArrayAdapter.getPosition(currentCourse));
            }
        });

        courseSpinner = view.findViewById(R.id.course_spinner);
        typesChipGroup = view.findViewById(R.id.types_list);
        instructorsChipGroup = view.findViewById(R.id.instructors_list);

        startTimeInput = view.findViewById(R.id.start_time_input);
        endTimeInput = view.findViewById(R.id.end_time_input);
        buildingInput = view.findViewById(R.id.audience_building_input);
        cabinetInput = view.findViewById(R.id.audience_cabinet_input);

        typesChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip c = group.findViewById(checkedId);
            if (c != null) {
                classSetupViewModel.setType((CourseTypes) c.getTag());
            } else {
                classSetupViewModel.setType(null);
            }
        });

        instructorsChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip c = group.findViewById(checkedId);
            if (c != null) {
                classSetupViewModel.setInstructorId(((InstructorMinimised) c.getTag()).id);
            } else {
                classSetupViewModel.setInstructorId(-1);
            }
        });

        startTimeInput.setOnClickListener(v -> openTimePickerDialog(startTimeInput));

        endTimeInput.setOnClickListener(v -> openTimePickerDialog(endTimeInput));

        buildingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                classSetupViewModel.setBuildingNumber(s.toString());
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
                classSetupViewModel.setCabinetNumber(s.toString());
            }
        });

        coursesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                if (obj != null) {
                    if (obj instanceof CourseWithInstructors) {
                        CourseWithInstructors selected = (CourseWithInstructors) obj;
                        classSetupViewModel.setCourseId(selected.course.id);
                        setupTypesChipGroup(selected);
                        setupInstructorsChipGroup(selected);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                classSetupViewModel.setCourseId(-1);
                classSetupViewModel.setType(null);
                classSetupViewModel.setInstructorId(-1);
            }
        });

        courseSpinner.setAdapter(coursesArrayAdapter);

        if (getArguments() != null) {
            classSetupViewModel.setDayId(getArguments().getInt("dayId"));
            id = getArguments().getInt("classId");

            isEditionMode = id > 0;
            if (isEditionMode) {
                currentClass = classViewModel.findClassById2(id);
                currentCourse = courseViewModel.findCourseById(currentClass.course.id);

                buildingInput.setText(String.valueOf(currentClass.aClass.audienceBuilding));
                cabinetInput.setText(String.valueOf(currentClass.aClass.audienceCabinet));

                setFormattedTime(currentClass.aClass.startTime, currentClass.aClass.endTime);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        classSetupViewModel.canBeSaved().observe(getViewLifecycleOwner(), confirm::setEnabled);

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

    private void setupInstructorsChipGroup(CourseWithInstructors selected) {
        instructorsChipGroup.clearCheck();
        instructorsChipGroup.removeAllViewsInLayout();

        if (selected.instructors.size() == 1) {
            InstructorMinimised i = selected.instructors.get(0);
            Chip c = (Chip) LayoutInflater.from(requireContext())
                    .inflate(R.layout.chip_instructor_action, instructorsChipGroup, false);
            c.setText(i.getShortName());
            c.setClickable(false);
            c.setCheckable(true);
            c.setTag(i);
            instructorsChipGroup.addView(c);
            instructorsChipGroup.check(c.getId());
        } else {
            for (InstructorMinimised i : selected.instructors) {
                Chip c = (Chip) LayoutInflater.from(requireContext())
                        .inflate(R.layout.chip_instructor_action, instructorsChipGroup, false);
                c.setText(i.getShortName());
                c.setClickable(true);
                c.setCheckable(true);
                c.setTag(i);
                instructorsChipGroup.addView(c);
            }
        }

        if (isEditionMode) {
            if (currentClass.instructor != null) {
                Chip c = instructorsChipGroup.findViewWithTag(currentClass.instructor);
                if (c != null) {
                    instructorsChipGroup.check(c.getId());
                }
            }
        }
    }

    private void setupTypesChipGroup(CourseWithInstructors selected) {
        typesChipGroup.clearCheck();
        typesChipGroup.removeAllViewsInLayout();

        if (selected.course.getCourseTypes().length == 1) {
            CourseTypes t = selected.course.getCourseTypes()[0];
            Chip c = (Chip) LayoutInflater.from(requireContext())
                    .inflate(R.layout.chip_course_type_choice, typesChipGroup, false);
            c.setText(t.toString());
            c.setClickable(false);
            c.setCheckable(true);
            c.setTag(t);
            typesChipGroup.addView(c);
            typesChipGroup.check(c.getId());
        } else {
            for (CourseTypes t : selected.course.getCourseTypes()) {
                Chip c = (Chip) LayoutInflater.from(requireContext())
                        .inflate(R.layout.chip_course_type_choice, typesChipGroup, false);
                c.setText(t.toString());
                c.setClickable(true);
                c.setCheckable(true);
                c.setTag(t);
                typesChipGroup.addView(c);
            }
        }

        if (isEditionMode) {
            if (currentClass.aClass.getType() != null) {
                Chip c = typesChipGroup.findViewWithTag(currentClass.aClass.getType()[0]);
                if (c != null) {
                    typesChipGroup.check(c.getId());
                }
            }
        }
    }

    private void setFormattedTime(Date startTime, Date endTime) {
        String startTimeString;
        String endTimeString;

        int timeFormatInt = preferences.getInt(SettingsFragment.timeFormatSharedKey, TimeFormats.system.ordinal());

        TimeFormats format = TimeFormats.values()[timeFormatInt];
        switch (format) {
            case system:
                startTimeString = defaultFormatter.format(startTime);
                endTimeString = defaultFormatter.format(endTime);
                break;
            case f12Hour:
                startTimeString = MainActivity.f12Hour.format(startTime);
                endTimeString = MainActivity.f12Hour.format(endTime);
                break;
            case f24Hour:
                startTimeString = MainActivity.f24Hour.format(startTime);
                endTimeString = MainActivity.f24Hour.format(endTime);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + format);
        }

        startTimeInput.setText(startTimeString);
        endTimeInput.setText(endTimeString);
    }

    private void openTimePickerDialog(TextInputEditText field) {
        Calendar calendar = Calendar.getInstance();

        String fieldContent = Objects.requireNonNull(field.getText()).toString();

        int hour;
        int minutes;
        boolean is24HourFormat;

        int timeFormatInt = preferences.getInt(SettingsFragment.timeFormatSharedKey, TimeFormats.system.ordinal());

        TimeFormats format = TimeFormats.values()[timeFormatInt];

        if (format == TimeFormats.system) {
            is24HourFormat = android.text.format.DateFormat.is24HourFormat(requireActivity().getApplicationContext());
        } else {
            is24HourFormat = format == TimeFormats.f24Hour;
        }

        if (!fieldContent.isEmpty()) {
            Time time = new Time(0);
            try {
                time.setTime(defaultFormatter.parse(fieldContent).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                try {
                    time.setTime(MainActivity.f12Hour.parse(fieldContent).getTime());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    try {
                        time.setTime(MainActivity.f24Hour.parse(fieldContent).getTime());
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            String timeString = defaultFormatter.format(time);

            int delIndex = timeString.indexOf(":");

            String hourString = timeString.substring(0, delIndex);
            String minuteString = timeString.substring(delIndex + 1, delIndex + 3);

            hour = Integer.parseInt(hourString);
            minutes = Integer.parseInt(minuteString);
        } else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
        }

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(is24HourFormat ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minutes)
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            String time = timePicker.getHour() + ":" + timePicker.getMinute();
            Time newTime = new Time(0);
            try {
                newTime.setTime(MainActivity.f24Hour.parse(time).getTime());
                time = defaultFormatter.format(newTime);

                field.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        timePicker.showNow(getParentFragmentManager(), "time picker dialog");
    }

    private void updateClass() {
        currentClass.aClass.courseId = classSetupViewModel.getCourseId();
        currentClass.aClass.instructorId = classSetupViewModel.getInstructorId();
        currentClass.aClass.setType(classSetupViewModel.getType());
        currentClass.aClass.audienceBuilding = classSetupViewModel.getBuildingNumber();
        currentClass.aClass.audienceCabinet = classSetupViewModel.getCabinetNumber();

        String startTime = Objects.requireNonNull(startTimeInput.getText()).toString();
        String endTime = Objects.requireNonNull(endTimeInput.getText()).toString();

        try {
            currentClass.aClass.startTime = defaultFormatter.parse(startTime);
            currentClass.aClass.endTime = defaultFormatter.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        classViewModel.update(currentClass.aClass);
    }

    private void addNewClass() {
        Class newClass = new Class(
                classSetupViewModel.getCourseId(),
                classSetupViewModel.getDayId(),
                classSetupViewModel.getInstructorId(),
                classSetupViewModel.getBuildingNumber(),
                classSetupViewModel.getCabinetNumber(),
                classSetupViewModel.getType()
        );

        String startTime = Objects.requireNonNull(startTimeInput.getText()).toString();
        String endTime = Objects.requireNonNull(endTimeInput.getText()).toString();

        try {
            newClass.startTime = defaultFormatter.parse(startTime);
            newClass.endTime = defaultFormatter.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        classViewModel.insert(newClass);
    }
}
