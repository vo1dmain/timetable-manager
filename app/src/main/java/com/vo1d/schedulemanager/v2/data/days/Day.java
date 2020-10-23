package com.vo1d.schedulemanager.v2.data.days;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.weeks.Week;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "day_table",
        foreignKeys = {
                @ForeignKey(entity = Week.class, parentColumns = "id", childColumns = "weekId", onDelete = CASCADE)
        },
        indices = {
                @Index(value = {"id"}, unique = true),
                @Index(value = {"weekId"})
        })
public class Day implements IMyEntity {

    public final int order;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(defaultValue = "1")
    public int weekId;

    public Day(int order, int weekId) {
        this.order = order;
        this.weekId = weekId;
    }

    public Day(DaysOfWeek name, int weekId) {
        this.order = name.ordinal();
        this.weekId = weekId;
    }
}
