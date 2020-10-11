package com.vo1d.schedulemanager.v2.data.day;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "day_table", indices = {@Index(value = {"id"}, unique = true)})
public class Day {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int order;

    public Day(int order) {
        this.order = order;
    }

    //public Day(DaysOfTheWeek order) {
    //    this.order = order.ordinal();
    //}

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
