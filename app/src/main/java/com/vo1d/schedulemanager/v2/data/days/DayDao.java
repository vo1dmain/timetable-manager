package com.vo1d.schedulemanager.v2.data.days;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class DayDao implements IBaseDao<Day> {

    @Transaction
    @Query("SELECT * FROM day_table ORDER BY `order` ASC")
    abstract LiveData<List<Day>> getAll();
}
