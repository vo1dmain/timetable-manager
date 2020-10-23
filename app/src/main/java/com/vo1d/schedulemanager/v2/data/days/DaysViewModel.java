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

    public void insert(Day... d) {
        repository.insert(d);
    }

    public void update(Day d) {
        repository.update(d);
    }

    public void delete(Day... d) {
        repository.delete(d);
    }

    public LiveData<List<Day>> getAllDays() {
        return allDays;
    }
}
