package com.vo1d.schedulemanager.v2.data.weeks;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class WeekDao implements IBaseDao<Week> {
    @Transaction
    @Query("SELECT * FROM week_table ORDER BY title ASC")
    abstract LiveData<List<WeekWithDays>> getAll();

    @Transaction
    @Query("SELECT * FROM week_table WHERE id = :id")
    abstract LiveData<WeekWithDays> findWeekById(int id);
}
