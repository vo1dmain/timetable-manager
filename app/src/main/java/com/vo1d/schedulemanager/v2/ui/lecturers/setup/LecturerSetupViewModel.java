package com.vo1d.schedulemanager.v2.ui.lecturers.setup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LecturerSetupViewModel extends ViewModel {

    public final MutableLiveData<Boolean> firstNameIsNotEmpty = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> middleNameIsNotEmpty = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> lastNameIsNotEmpty = new MutableLiveData<>(false);

    public final MutableLiveData<Boolean> canBeSaved = new MutableLiveData<>(false);

    private boolean _firstNameIsNotEmpty = false;
    private boolean _middleNameIsNotEmpty = false;
    private boolean _lastNameIsNotEmpty = false;

    public LecturerSetupViewModel() {
        firstNameIsNotEmpty.observeForever(aBoolean -> {
            _firstNameIsNotEmpty = aBoolean;
            canBeSaved.setValue(_firstNameIsNotEmpty && _middleNameIsNotEmpty && _lastNameIsNotEmpty);
        });

        middleNameIsNotEmpty.observeForever(aBoolean -> {
            _middleNameIsNotEmpty = aBoolean;
            canBeSaved.setValue(_firstNameIsNotEmpty && _middleNameIsNotEmpty && _lastNameIsNotEmpty);
        });

        lastNameIsNotEmpty.observeForever(aBoolean -> {
            _lastNameIsNotEmpty = aBoolean;
            canBeSaved.setValue(_firstNameIsNotEmpty && _middleNameIsNotEmpty && _lastNameIsNotEmpty);
        });
    }
}
