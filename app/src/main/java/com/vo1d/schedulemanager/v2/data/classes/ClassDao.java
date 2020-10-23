package com.vo1d.schedulemanager.v2.data.classes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class ClassDao implements IBaseDao<Class> {
    @Transaction
    @Query("DELETE FROM class_table")
    abstract void deleteAll();

    @Transaction
    @Query("SELECT * FROM class_table WHERE id=:id")
    abstract ClassWithSubject findClassById2(int id);

    @Transaction
    @Query("SELECT * FROM class_table WHERE dayId=:dayId ORDER BY startTimeHour ASC, startTimeMinutes ASC")
    abstract LiveData<List<ClassWithSubject>> findAllClassesForADay2(int dayId);

    @Transaction
    @Query("SELECT * FROM class_table WHERE dayId=:dayId ORDER BY startTimeHour ASC, startTimeMinutes ASC")
    abstract Class[] findAllClassesForADayAsArray(int dayId);
}
