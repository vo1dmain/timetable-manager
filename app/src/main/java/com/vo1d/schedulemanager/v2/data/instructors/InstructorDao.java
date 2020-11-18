package com.vo1d.schedulemanager.v2.data.instructors;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class InstructorDao implements IBaseDao<Instructor> {

    @Transaction
    @Query("DELETE FROM instructor_table")
    abstract void deleteAll();

    @Transaction
    @Query("SELECT * FROM instructor_table WHERE id=:id")
    abstract InstructorWithJunctions findById(int id);

    @Transaction
    @Query("SELECT * FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    abstract LiveData<List<InstructorWithJunctions>> getAll();

    @Transaction
    @Query("SELECT * FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    abstract List<InstructorWithJunctions> getAllAsList();

    @Transaction
    @Query("SELECT `id`, `firstName`, `middleName`, `lastName` FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    abstract List<InstructorMinimised> getAllMinimisedAsList();

    @Transaction
    @Query("SELECT * FROM instructor_table " +
            "WHERE (lastName LIKE :filter || '%')" +
            "OR (middleName LIKE :filter || '%') " +
            "OR (firstName LIKE :filter || '%')" +
            "ORDER BY lastName ASC, middleName ASC, firstName ASC")
    abstract List<InstructorWithJunctions> getFiltered(String filter);
}
