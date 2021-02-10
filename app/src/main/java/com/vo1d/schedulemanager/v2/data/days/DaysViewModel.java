package com.vo1d.schedulemanager.v2.data.days;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class DaysViewModel extends AndroidViewModel {

    private final DayRepository repository;
    //private final LiveData<List<Day>> allDays;

    public DaysViewModel(@NonNull Application application) {
        super(application);
        repository = new DayRepository(application);
        //allDays = repository.getAll();
    }

    public void insert(Day... d) {
        repository.insert(d);
    }

// --Commented out by Inspection START (10.02.2021 15:21):
//    public void update(Day d) {
//        repository.update(d);
//    }
// --Commented out by Inspection STOP (10.02.2021 15:21)

    public void delete(Day... d) {
        repository.delete(d);
    }

// --Commented out by Inspection START (10.02.2021 15:18):
//    public LiveData<List<Day>> getAllDays() {
//        return allDays;
//    }
// --Commented out by Inspection STOP (10.02.2021 15:18)
}
