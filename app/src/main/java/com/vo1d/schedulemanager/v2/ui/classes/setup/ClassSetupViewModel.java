package com.vo1d.schedulemanager.v2.ui.classes.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassSetupViewModel extends ViewModel {
    private final List<CourseWithInstructors> coursesList = new ArrayList<>(Collections.emptyList());

    private final MutableLiveData<Boolean> buildingIsFilled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> cabinetIsFilled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> courseIsSet = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> instructorIsSet = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> courseTypeIsSet = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> canBeSaved = new MutableLiveData<>(false);

    private boolean _courseIsSet = false;
    private boolean _instructorIsSet = false;
    private boolean _courseTypeIsSet = false;
    private boolean _buildingIsFiled = false;
    private boolean _cabinetIsFilled = false;
    private int dayId;
    private int courseId;
    private int instructorId;
    private int buildingNumber;
    private int cabinetNumber;
    private CourseTypes type;

    public ClassSetupViewModel() {
        courseIsSet.observeForever(aBoolean -> {
            _courseIsSet = aBoolean;
            boolean allIsSet = _courseIsSet &&
                    _courseTypeIsSet &&
                    _instructorIsSet &&
                    _buildingIsFiled &&
                    _cabinetIsFilled;
            canBeSaved.setValue(allIsSet);
        });

        courseTypeIsSet.observeForever(aBoolean -> {
            _courseTypeIsSet = aBoolean;
            boolean allIsSet = _courseIsSet &&
                    _courseTypeIsSet &&
                    _instructorIsSet &&
                    _buildingIsFiled &&
                    _cabinetIsFilled;
            canBeSaved.setValue(allIsSet);
        });

        instructorIsSet.observeForever(aBoolean -> {
            _instructorIsSet = aBoolean;
            boolean allIsSet = _courseIsSet &&
                    _courseTypeIsSet &&
                    _instructorIsSet &&
                    _buildingIsFiled &&
                    _cabinetIsFilled;
            canBeSaved.setValue(allIsSet);
        });

        buildingIsFilled.observeForever(aBoolean -> {
            _buildingIsFiled = aBoolean;
            boolean allIsSet = _courseIsSet &&
                    _courseTypeIsSet &&
                    _instructorIsSet &&
                    _buildingIsFiled &&
                    _cabinetIsFilled;
            canBeSaved.setValue(allIsSet);
        });

        cabinetIsFilled.observeForever(aBoolean -> {
            _cabinetIsFilled = aBoolean;
            boolean allIsSet = _courseIsSet &&
                    _courseTypeIsSet &&
                    _instructorIsSet &&
                    _buildingIsFiled &&
                    _cabinetIsFilled;
            canBeSaved.setValue(allIsSet);
        });
    }

    public LiveData<Boolean> canBeSaved() {
        return canBeSaved;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
        courseIsSet.postValue(courseId != -1);
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
        instructorIsSet.postValue(instructorId != -1);
    }

    public CourseTypes getType() {
        return type;
    }

    public void setType(CourseTypes type) {
        this.type = type;
        courseTypeIsSet.postValue(type != null);
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = Integer.parseInt(!buildingNumber.isEmpty() ? buildingNumber : "-1");
        buildingIsFilled.postValue(!buildingNumber.isEmpty());
    }

    public int getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = Integer.parseInt(!cabinetNumber.isEmpty() ? cabinetNumber : "-1");
        cabinetIsFilled.postValue(!cabinetNumber.isEmpty());
    }

    public List<CourseWithInstructors> getCoursesList() {
        return coursesList;
    }
}
