package com.vo1d.schedulemanager.v2.data.day;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DaysViewModel extends AndroidViewModel {

    private DaysRepository repository;
    LiveData<List<Day>> allDays;

    public DaysViewModel(@NonNull Application application) {
        super(application);
        repository = new DaysRepository(application);
        allDays = repository.getAllDays();
    }

    /*public void insert(Day day) {
        repository.insert(day);
    }

    public void update(Day day) {
        repository.update(day);
    }

    public void delete(Day day) {
        repository.delete(day);
    }
    */

    public LiveData<List<Day>> getAllDays() {
        return allDays;
    }
}
