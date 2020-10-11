package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithSubject;
import com.vo1d.schedulemanager.v2.data.classes.ClassesRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ClassesListViewModel extends AndroidViewModel {

    private final ClassesRepository repo;
    private LiveData<List<ClassWithSubject>> allClassesForADay2;
    private LiveData<List<Class>> selected = new MutableLiveData<>(new LinkedList<>());

    public ClassesListViewModel(@NonNull Application application) {
        super(application);

        repo = new ClassesRepository(application);
    }
    public LiveData<List<ClassWithSubject>> getAllClassesForADay2() {
        return allClassesForADay2;
    }

    public void setAllClassesForADay2(LiveData<List<ClassWithSubject>> allClassesForADay) {
        this.allClassesForADay2 = allClassesForADay;
    }

    public void deleteSelectedClasses() {
        repo.delete(Objects.requireNonNull(selected.getValue()).toArray(new Class[0]));

        selected.getValue().clear();
    }

    public void addToSelection(Class c) {
        Objects.requireNonNull(selected.getValue()).add(c);
    }

    public void removeFromSelection(Class c) {
        Objects.requireNonNull(selected.getValue()).remove(c);
    }

    public void clearSelection() {
        Objects.requireNonNull(selected.getValue()).clear();
    }

    public LiveData<List<Class>> getSelectedClasses() {
        return selected;
    }
}
