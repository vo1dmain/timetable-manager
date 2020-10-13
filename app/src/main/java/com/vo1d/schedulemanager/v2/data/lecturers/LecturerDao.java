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
    @Query("SELECT * FROM lecturer_table ORDER BY firstName ASC, middleName ASC, lastName ASC")
    abstract LiveData<List<Lecturer>> getAll();

    @Transaction
    @Query("SELECT * FROM lecturer_table " +
            "WHERE (firstName LIKE :filter || '%') " +
            "OR (middleName LIKE :filter || '%') " +
            "OR (lastName LIKE :filter || '%')  " +
            "ORDER BY firstName ASC, middleName ASC, lastName ASC")
    abstract List<Lecturer> getFilteredLecturers(String filter);
}
