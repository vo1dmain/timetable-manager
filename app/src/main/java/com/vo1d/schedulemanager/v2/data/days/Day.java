package com.vo1d.schedulemanager.v2.data.days;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

@Entity(tableName = "day_table", indices = {@Index(value = {"id"}, unique = true)})
public class Day implements IMyEntity {

    private final int order;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Day(int order) {
        this.order = order;
    }

    public Day(NamesOfDays name) {
        this.order = name.ordinal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    //public void setOrder(int order) {
    //    this.order = order;
    //}
}
