package com.vo1d.schedulemanager.v2.data.courses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class CourseDao implements IBaseDao<Course> {

    @Transaction
    @Query("DELETE FROM course_table")
    abstract void deleteAll();

    @Transaction
    @Query("SELECT * FROM course_table ORDER BY title ASC")
    abstract LiveData<List<CourseWithInstructors>> getAll();

    @Transaction
    @Query("SELECT * FROM course_table WHERE title LIKE :filter || '%' ORDER BY title ASC")
    abstract List<CourseWithInstructors> getFiltered(String filter);

    @Transaction
    @Query("SELECT * FROM course_table WHERE id=:id")
    abstract CourseWithInstructors findById(int id);
}
