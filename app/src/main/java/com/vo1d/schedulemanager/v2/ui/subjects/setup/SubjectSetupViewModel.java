package com.vo1d.schedulemanager.v2.ui.subjects.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vo1d.schedulemanager.v2.data.subject.SubjectTypes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SubjectSetupViewModel extends ViewModel {

    public final MutableLiveData<Boolean> titleIsFilled = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> lecturerIsFilled = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> typeIsChecked = new MutableLiveData<>(false);

    public final MutableLiveData<Boolean> canBeSaved = new MutableLiveData<>(false);

    private final MutableLiveData<List<SubjectTypes>> selectedTypes = new MutableLiveData<>(new LinkedList<>());

    private boolean _titleIsFiled = false;
    private boolean _lecturerIsFilled = false;
    private boolean _typeIsChecked = false;

    public SubjectSetupViewModel() {
        titleIsFilled.observeForever(aBoolean -> {
            _titleIsFiled = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _lecturerIsFilled && _typeIsChecked);
        });

        lecturerIsFilled.observeForever(aBoolean -> {
            _lecturerIsFilled = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _lecturerIsFilled && _typeIsChecked);
        });

        typeIsChecked.observeForever(aBoolean -> {
            _typeIsChecked = aBoolean;
            canBeSaved.setValue(_titleIsFiled && _lecturerIsFilled && _typeIsChecked);
        });

        selectedTypes.observeForever(subjectTypes -> typeIsChecked.postValue(!subjectTypes.isEmpty()));
    }

    public void addToSelection(SubjectTypes type) {
        Objects.requireNonNull(selectedTypes.getValue());

        List<SubjectTypes> list = Objects.requireNonNull(selectedTypes.getValue());

        list.add(type);

        selectedTypes.postValue(list);
    }

    public void removeFromSelection(SubjectTypes type) {
        Objects.requireNonNull(selectedTypes.getValue());

        List<SubjectTypes> list = Objects.requireNonNull(selectedTypes.getValue());

        list.remove(type);

        selectedTypes.postValue(list);
    }

    public SubjectTypes[] getSelectedTypesAsArray() {
        Collections.sort(Objects.requireNonNull(selectedTypes.getValue()));
        return Objects.requireNonNull(selectedTypes.getValue()).toArray(new SubjectTypes[0]);
    }

    public LiveData<List<SubjectTypes>> getSelectedTypes() {
        return selectedTypes;
    }
}
