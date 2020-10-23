package com.vo1d.schedulemanager.v2.data.weeks;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.days.Day;

import java.util.List;

public class WeekWithDays {
    @Embedded
    public Week week;

    @Relation(parentColumn = "id", entityColumn = "weekId")
    public List<Day> days;
}
