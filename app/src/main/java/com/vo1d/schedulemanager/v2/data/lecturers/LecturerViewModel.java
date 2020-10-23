package com.vo1d.schedulemanager.v2.data.lecturers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LecturerViewModel extends AndroidViewModel {

    private final LecturerRepository repository;
    private final LiveData<List<Lecturer>> allLecturers;

    public LecturerViewModel(@NonNull Application application) {
        super(application);
        repository = new LecturerRepository(application);
        allLecturers = repository.getAll();
    }

    public void insert(Lecturer... l) {
        repository.insert(l);
    }

    public void update(Lecturer l) {
        repository.update(l);
    }

    public void delete(Lecturer... l) {
        repository.delete(l);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Lecturer> getFilteredLecturers(String filter) {
        return repository.getFilteredLecturers(filter);
    }

    public Lecturer findLecturerById(int id) {
        return repository.findLecturerById(id);
    }

    public LiveData<List<Lecturer>> getAllLecturers() {
        return allLecturers;
    }
}
