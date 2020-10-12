package com.vo1d.schedulemanager.v2.data.lecturers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class LecturerDao implements IBaseDao<Lecturer> {

    @Transaction
    @Query("DELETE FROM lecturer_table")
    abstract void deleteAll();

    @Query("SELECT * FROM lecturer_table WHERE id=:id")
    abstract Lecturer findLecturerById(int id);

    @Transaction
    @Query("SELECT * FROM lecturer_table ORDER BY lastName ASC, firstName ASC, middleName ASC")
    abstract LiveData<List<Lecturer>> getAll();

    @Transaction
    @Query("SELECT * FROM lecturer_table " +
            "WHERE (lastName OR firstName OR middleName) LIKE '%' || :filter || '%' " +
            "ORDER BY lastName ASC, firstName ASC, middleName ASC")
    abstract List<Lecturer> getFilteredLecturers(String filter);
}
