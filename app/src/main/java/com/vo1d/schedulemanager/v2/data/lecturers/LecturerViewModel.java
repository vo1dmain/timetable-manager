package com.vo1d.schedulemanager.v2.data.lecturers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LecturerViewModel extends AndroidViewModel {

    private LecturerRepository repository;
    private LiveData<List<Lecturer>> allLecturers;

    public LecturerViewModel(@NonNull Application application) {
        super(application);
        repository = new LecturerRepository(application);
        allLecturers = repository.getAll();
    }

    public long insert(Lecturer lecturer) {
        return repository.insert(lecturer);
    }

    public void update(Lecturer lecturer) {
        repository.update(lecturer);
    }

    public void delete(Lecturer... lecturers) {
        repository.delete(lecturers);
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
