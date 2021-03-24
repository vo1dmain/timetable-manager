package com.vo1d.schedulemanager.v2.data.instructors;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public interface InstructorDao extends IBaseDao<Instructor> {

    @Transaction
    @Query("DELETE FROM instructor_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM instructor_table WHERE id=:id")
    InstructorWithJunctions findById(int id);

    @Transaction
    @Query("SELECT * FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    LiveData<List<InstructorWithJunctions>> getAll();

    @Transaction
    @Query("SELECT * FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    List<InstructorWithJunctions> getAllAsList();

    @Transaction
    @Query("SELECT `id`, `firstName`, `middleName`, `lastName` FROM instructor_table ORDER BY lastName ASC, middleName ASC, firstName ASC")
    List<InstructorMinimised> getAllMinimisedAsList();

    @Transaction
    @Query("SELECT * FROM instructor_table " +
            "WHERE (lastName LIKE :filter || '%')" +
            "OR (middleName LIKE :filter || '%') " +
            "OR (firstName LIKE :filter || '%')" +
            "ORDER BY lastName ASC, middleName ASC, firstName ASC")
    List<InstructorWithJunctions> getFiltered(String filter);
}
