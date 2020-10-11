package com.vo1d.schedulemanager.v2.data.subject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SubjectsViewModel extends AndroidViewModel {
    private SubjectsRepository repository;
    private LiveData<List<Subject>> allSubjects;
    private LiveData<List<Subject>> selectedSubjects = new MutableLiveData<>(new LinkedList<>());

    public SubjectsViewModel(@NonNull Application application) {
        super(application);
        repository = new SubjectsRepository(application);
        allSubjects = repository.getAllSubjects();
    }

    public long insert(Subject subject) {
        return repository.insert(subject);
    }

    public void update(Subject subject) {
        repository.update(subject);
    }

    public void delete(Subject... subjects) {
        repository.delete(subjects);
    }

    public void deleteAllSubjects() {
        repository.deleteAll();
    }

    public void deleteSelectedSubjects() {
        delete(Objects.requireNonNull(selectedSubjects.getValue()).toArray(new Subject[0]));

        selectedSubjects.getValue().clear();
    }

    public void addToSelection(Subject subject) {
        Objects.requireNonNull(selectedSubjects.getValue()).add(subject);
    }

    public void removeFromSelection(Subject subject) {
        Objects.requireNonNull(selectedSubjects.getValue()).remove(subject);
    }

    public void clearSelection() {
        Objects.requireNonNull(selectedSubjects.getValue()).clear();
    }

    public LiveData<List<Subject>> getSelectedSubjects() {
        return selectedSubjects;
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
