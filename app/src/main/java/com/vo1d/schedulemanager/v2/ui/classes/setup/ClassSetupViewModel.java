package com.vo1d.schedulemanager.v2.ui.classes.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassSetupViewModel extends ViewModel {
    private final MutableLiveData<Boolean> buildingIsFilled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> cabinetIsFilled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> subjectIsSet = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> subjectTypeIsSet = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> canBeSaved = new MutableLiveData<>(false);
    private boolean _buildingIsFiled = false;
    private boolean _cabinetIsFilled = false;
    private boolean _subjectIsSet = false;
    private boolean _subjectTypeIsSet = false;
    private int dayId;
    private int subjectId;
    private int buildingNumber;
    private int cabinetNumber;
    private SubjectTypes type;
    private List<Subject> subjectsList = new ArrayList<>(Collections.emptyList());

    public ClassSetupViewModel() {
        buildingIsFilled.observeForever(aBoolean -> {
            _buildingIsFiled = aBoolean;
            canBeSaved.setValue(_subjectIsSet && _subjectTypeIsSet && _buildingIsFiled && _cabinetIsFilled);
        });

        cabinetIsFilled.observeForever(aBoolean -> {
            _cabinetIsFilled = aBoolean;
            canBeSaved.setValue(_subjectIsSet && _subjectTypeIsSet && _buildingIsFiled && _cabinetIsFilled);
        });

        subjectIsSet.observeForever(aBoolean -> {
            _subjectIsSet = aBoolean;
            canBeSaved.setValue(_subjectIsSet && _subjectTypeIsSet && _buildingIsFiled && _cabinetIsFilled);
        });

        subjectTypeIsSet.observeForever(aBoolean -> {
            _subjectTypeIsSet = aBoolean;
            canBeSaved.setValue(_subjectIsSet && _subjectTypeIsSet && _buildingIsFiled && _cabinetIsFilled);
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
        subjectIsSet.postValue(subjectId != -1);
    }

    public SubjectTypes getType() {
        return type;
    }

    public void setType(SubjectTypes type) {
        this.type = type;
        subjectTypeIsSet.postValue(type != null);
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

    public List<Subject> getSubjectsList() {
        return subjectsList;
    }
}
