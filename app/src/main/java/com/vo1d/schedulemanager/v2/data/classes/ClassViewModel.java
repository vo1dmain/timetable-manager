package com.vo1d.schedulemanager.v2.data.classes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClassViewModel extends AndroidViewModel {
    private final ClassRepository repository;

    public ClassViewModel(@NonNull Application application) {
        super(application);
        repository = new ClassRepository(application);
    }

    public void insert(Class... c) {
        repository.insert(c);
    }

    public void update(Class c) {
        repository.update(c);
    }

    public void delete(Class... c) {
        repository.delete(c);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public ClassWithCourse findClassById2(int id) {
        return repository.findClassById2(id);
    }

    public LiveData<List<ClassWithCourse>> findAllClassesForADay2(int dayId) {
        return repository.findAllClassesForADay2(dayId);
    }

    public Class[] findAllClassesForADayAsArray(int dayId) {
        return repository.findAllClassesForADayAsArray(dayId);
    }
}
