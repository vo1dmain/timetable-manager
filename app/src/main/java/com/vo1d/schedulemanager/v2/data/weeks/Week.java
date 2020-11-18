package com.vo1d.schedulemanager.v2.data.weeks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;

import java.util.List;

@Entity(tableName = "week_table", indices = {@Index(value = {"id"}, unique = true)})
public class Week implements IMyEntity, Comparable<Week> {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    @TypeConverters({DaysOfWeekToStringConverter.class})
    @ColumnInfo(defaultValue = "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday")
    public List<DaysOfWeek> availableDays;

    public Week(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Week week) {
        return this.id - week.id;
    }
}
