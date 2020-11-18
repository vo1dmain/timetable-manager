package com.vo1d.schedulemanager.v2.data.courseinstructor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

@Dao
public interface CourseInstructorDao extends IBaseDao<CourseInstructor> {
    @Override
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseInstructor... pairs);

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(CourseInstructor... pairs);

    @Transaction
    @Query("DELETE FROM course_instructor_table")
    void deleteAll();
}
