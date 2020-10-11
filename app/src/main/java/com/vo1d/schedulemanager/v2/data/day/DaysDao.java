package com.vo1d.schedulemanager.v2.data.day;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.BaseDao;

import java.util.List;

@Dao
public abstract class DaysDao implements BaseDao<Day> {

    @Transaction
    @Query("SELECT * FROM day_table ORDER BY `order` ASC")
    abstract LiveData<List<Day>> getAllDays();

    //@Transaction
    //@Query("SELECT * FROM day_table ORDER BY `order` ASC")
    //abstract LiveData<List<DayWithClasses>> getAllDays2();
}
