package com.vo1d.schedulemanager.v2.data.courses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructorRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private final CourseRepository repository;
    private final CourseInstructorRepository junctionRepo;
    private final LiveData<List<CourseWithInstructors>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        junctionRepo = new CourseInstructorRepository(application);
        allCourses = repository.getAll();
    }

    public long insert(Course s) {
        return repository.insert(s);
    }

    public void insertJunctions(CourseInstructor... junctions) {
        junctionRepo.insert(junctions);
    }

    public void update(Course s) {
        repository.update(s);
    }

    public void delete(Course... s) {
        repository.delete(s);
    }

    public void deleteJunctions(CourseInstructor... junctions) {
        junctionRepo.delete(junctions);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<CourseWithInstructors> getFiltered(String filter) {
        return repository.getFiltered(filter);
    }

    public LiveData<List<CourseWithInstructors>> getAll() {
        return allCourses;
    }

    public CourseWithInstructors findCourseById(int id) {
        return repository.findById(id);
    }
}
