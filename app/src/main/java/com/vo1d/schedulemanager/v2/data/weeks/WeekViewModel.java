package com.vo1d.schedulemanager.v2.data.weeks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeekViewModel extends AndroidViewModel {
    private final WeekRepository repository;

    private final LiveData<List<WeekWithDays>> allWeeks;

    public WeekViewModel(@NonNull Application application) {
        super(application);
        repository = new WeekRepository(application);
        allWeeks = repository.getAllWeeks();
    }

    public void insert(Week... w) {
        repository.insert(w);
    }

    public void update(Week w) {
        repository.update(w);
    }

    public void delete(Week... w) {
        repository.delete(w);
    }

    public LiveData<List<WeekWithDays>> getAllWeeks() {
        return allWeeks;
    }

// --Commented out by Inspection START (10.02.2021 15:16):
//    public LiveData<WeekWithDays> findWeekById(int id) {
//        return repository.findWeekById(id);
//    }
// --Commented out by Inspection STOP (10.02.2021 15:16)
}
