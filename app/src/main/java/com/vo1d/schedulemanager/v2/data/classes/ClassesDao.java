package com.vo1d.schedulemanager.v2.data.classes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.BaseDao;

import java.util.List;

@Dao
public abstract class ClassesDao implements BaseDao<Class> {
    @Transaction
    @Query("DELETE FROM class_table")
    abstract void deleteAll();


    @Query("SELECT * FROM class_table WHERE id=:id")
    abstract Class findClassById(int id);

    @Query("SELECT * FROM class_table WHERE id=:id")
    abstract ClassWithSubject findClassById2(int id);

    @Transaction
    @Query("SELECT * FROM class_table WHERE dayId=:dayId ORDER BY `startTime` ASC")
    abstract LiveData<List<Class>> findAllClassesForADay(int dayId);

    @Transaction
    @Query("SELECT * FROM class_table WHERE dayId=:dayId ORDER BY `startTime` ASC")
    abstract LiveData<List<ClassWithSubject>> findAllClassesForADay2(int dayId);
}
