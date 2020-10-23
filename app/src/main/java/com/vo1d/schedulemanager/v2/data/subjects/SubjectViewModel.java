package com.vo1d.schedulemanager.v2.data.subjects;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private final SubjectRepository repository;
    private final LiveData<List<Subject>> allSubjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        repository = new SubjectRepository(application);
        allSubjects = repository.getAllSubjects();
    }

    public void insert(Subject... s) {
        repository.insert(s);
    }

    public void update(Subject s) {
        repository.update(s);
    }

    public void delete(Subject... s) {
        repository.delete(s);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Subject> getFilteredSubjects(String filter) {
        return repository.getFilteredSubjects(filter);
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public Subject findSubjectById(int id) {
        return repository.findSubjectById(id);
    }
}
