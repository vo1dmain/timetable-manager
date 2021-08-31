package com.vo1d.schedulemanager.v2.data.courses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public interface CourseDao extends IBaseDao<Course> {

    @Transaction
    @Query("DELETE FROM course_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM course_table ORDER BY title ASC")
    LiveData<List<CourseWithInstructors>> getAll();

    @Transaction
    @Query("SELECT * FROM course_table " +
            "WHERE title LIKE '%' || :filter || '%'" +
            "ORDER BY title ASC")
    List<CourseWithInstructors> getFiltered(String filter);

    @Transaction
    @Query("SELECT * FROM course_table WHERE id=:id")
    CourseWithInstructors findById(int id);
}
