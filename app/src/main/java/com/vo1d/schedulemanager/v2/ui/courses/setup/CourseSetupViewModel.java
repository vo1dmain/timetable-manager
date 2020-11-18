package com.vo1d.schedulemanager.v2.ui.courses.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorMinimised;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CourseSetupViewModel extends ViewModel {

    public final MutableLiveData<Boolean> titleIsFilled = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> instructorIsSet = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> typeIsChecked = new MutableLiveData<>(false);

    public final MutableLiveData<Boolean> canBeSaved = new MutableLiveData<>(false);

    private final MutableLiveData<List<CourseTypes>> selectedTypes = new MutableLiveData<>(new LinkedList<>());
    private final MutableLiveData<List<InstructorMinimised>> selectedInstructors = new MutableLiveData<>(new LinkedList<>());

    private boolean _titleIsFiled = false;
    private boolean _instructorIsSet = false;
    private boolean _typeIsChecked = false;
    private List<InstructorMinimised> allInstructors;

    public CourseSetupViewModel() {
        titleIsFilled.observeForever(aBoolean -> {
            _titleIsFiled = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _instructorIsSet && _typeIsChecked);
        });

        instructorIsSet.observeForever(aBoolean -> {
            _instructorIsSet = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _instructorIsSet && _typeIsChecked);
        });

        typeIsChecked.observeForever(aBoolean -> {
            _typeIsChecked = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _instructorIsSet && _typeIsChecked);
        });

        selectedTypes.observeForever(courseTypes -> typeIsChecked.postValue(!courseTypes.isEmpty()));
        selectedInstructors.observeForever(instructors -> instructorIsSet.postValue(!instructors.isEmpty()));
    }

    public void addTypeToSelection(CourseTypes type) {
        Objects.requireNonNull(selectedTypes.getValue());

        List<CourseTypes> list = Objects.requireNonNull(selectedTypes.getValue());

        list.add(type);

        selectedTypes.postValue(list);
    }

    public void removeTypeFromSelection(CourseTypes type) {
        Objects.requireNonNull(selectedTypes.getValue());

        List<CourseTypes> list = Objects.requireNonNull(selectedTypes.getValue());

        list.remove(type);

        selectedTypes.postValue(list);
    }

    public CourseTypes[] getSelectedTypesAsArray() {
        Collections.sort(Objects.requireNonNull(selectedTypes.getValue()));
        return Objects.requireNonNull(selectedTypes.getValue()).toArray(new CourseTypes[0]);
    }

    public void setupInstructors(List<InstructorMinimised> allAsList) {
        this.allInstructors = allAsList;
    }

    public List<InstructorMinimised> getAllInstructors() {
        return allInstructors;
    }

    public LiveData<List<InstructorMinimised>> getSelectedInstructors() {
        return selectedInstructors;
    }

    public void addInstructorToSelection(InstructorMinimised instructor) {
        List<InstructorMinimised> list = Objects.requireNonNull(selectedInstructors.getValue());
        list.add(instructor);
        selectedInstructors.postValue(list);
    }

    public void removeInstructorFromSelection(InstructorMinimised instructor) {
        List<InstructorMinimised> list = Objects.requireNonNull(selectedInstructors.getValue());
        list.remove(instructor);
        selectedInstructors.postValue(list);
    }
}
