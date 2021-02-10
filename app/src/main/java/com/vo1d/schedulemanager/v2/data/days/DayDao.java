package com.vo1d.schedulemanager.v2.data.days;

import androidx.room.Dao;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

@Dao
public abstract class DayDao implements IBaseDao<Day> {

// --Commented out by Inspection START (10.02.2021 15:32):
//    @Transaction
//    @Query("SELECT * FROM day_table ORDER BY `order` ASC")
//    abstract LiveData<List<Day>> getAll();
// --Commented out by Inspection STOP (10.02.2021 15:32)
}
