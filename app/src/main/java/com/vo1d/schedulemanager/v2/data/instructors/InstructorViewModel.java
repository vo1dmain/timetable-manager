package com.vo1d.schedulemanager.v2.data.instructors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructorRepository;

import java.util.List;

public class InstructorViewModel extends AndroidViewModel {

    private final InstructorRepository repository;
    private final LiveData<List<InstructorWithJunctions>> allInstructors;
    private final CourseInstructorRepository junctionsRepo;

    public InstructorViewModel(@NonNull Application application) {
        super(application);
        repository = new InstructorRepository(application);
        junctionsRepo = new CourseInstructorRepository(application);
        allInstructors = repository.getAll();
    }

    public void insert(Instructor... i) {
        repository.insert(i);
    }

    public void insertJunctions(CourseInstructor... junctions) {
        junctionsRepo.insert(junctions);
    }

    public void update(Instructor i) {
        repository.update(i);
    }

    public void delete(Instructor... i) {
        repository.delete(i);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<InstructorWithJunctions> getFiltered(String filter) {
        return repository.getFiltered(filter);
    }

    public InstructorWithJunctions findById(int id) {
        return repository.findById(id);
    }

    public LiveData<List<InstructorWithJunctions>> getAll() {
        return allInstructors;
    }

    public List<InstructorWithJunctions> getAllAsList() {
        return repository.getAllAsList();
    }

    public List<InstructorMinimised> getAllMinimisedAsList() {
        return repository.getAllMinimisedAsList();
    }
}
