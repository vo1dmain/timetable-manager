package com.vo1d.schedulemanager.v2.data.days;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DaysViewModel extends AndroidViewModel {

    private final DayRepository repository;
    private final LiveData<List<Day>> allDays;

    public DaysViewModel(@NonNull Application application) {
        super(application);
        repository = new DayRepository(application);
        allDays = repository.getAll();
    }

    public void insert(Day day) {
        repository.insert(day);
    }

    public void update(Day day) {
        repository.update(day);
    }

    public void delete(Day... days) {
        repository.delete(days);
    }

    public LiveData<List<Day>> getAllDays() {
        return allDays;
    }
}
