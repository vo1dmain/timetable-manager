package com.vo1d.schedulemanager.v2.ui.schedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.weeks.WeekWithDays;

public class ScheduleFragmentViewModel extends AndroidViewModel {
    private final MutableLiveData<WeekWithDays> currentWeekLive = new MutableLiveData<>();
    private Day dayToDelete;
    private String[] daysNamesShort;
    private String[] daysNamesFull;

    private WeekWithDays currentWeek;

    private int currentDayNumber;

    public ScheduleFragmentViewModel(@NonNull Application application) {
        super(application);

        currentWeekLive.observeForever(week -> currentWeek = week);
    }

    public Day getDayToDelete() {
        return dayToDelete;
    }

    public void setDayToDelete(Day dayToDelete) {
        this.dayToDelete = dayToDelete;
    }

    public void setDaysNamesShort(String[] daysNamesShort) {
        this.daysNamesShort = daysNamesShort;
    }

    public void setDaysNamesFull(String[] daysNamesFull) {
        this.daysNamesFull = daysNamesFull;
    }

    public LiveData<WeekWithDays> getCurrentWeekLive() {
        return currentWeekLive;
    }

    public void setCurrentWeekLive(WeekWithDays currentWeekLive) {
        this.currentWeekLive.postValue(currentWeekLive);
    }

    public int getCurrentDayNumber() {
        return currentDayNumber;
    }

    public void setCurrentDayNumber(int currentDayNumber) {
        this.currentDayNumber = currentDayNumber;
    }

    public String getDayNameForPosition(int position) {
        return daysNamesShort[currentWeekLive.getValue().days.get(position).order];
    }

    public WeekWithDays getCurrentWeek() {
        return currentWeek;
    }

    public String getDayToDeleteName() {
        return daysNamesFull[dayToDelete.order];
    }
}
