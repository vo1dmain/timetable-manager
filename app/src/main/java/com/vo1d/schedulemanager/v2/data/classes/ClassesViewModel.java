package com.vo1d.schedulemanager.v2.data.classes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClassesViewModel extends AndroidViewModel {
    private ClassesRepository repository;

    public ClassesViewModel(@NonNull Application application) {
        super(application);
        repository = new ClassesRepository(application);
    }

    public long insert(Class c) {
        return repository.insert(c);
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

    public ClassWithSubject findClassById2(int id) {
        return repository.findClassById2(id);
    }

    public LiveData<List<ClassWithSubject>> findAllClassesForADay2(int dayId) {
        return repository.findAllClassesForADay2(dayId);
    }
}
