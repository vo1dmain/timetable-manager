package com.vo1d.schedulemanager.v2.data.courseinstructor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

@Dao
public interface CourseInstructorDao extends IBaseDao<CourseInstructor> {
    @Override
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseInstructor... pairs);

// --Commented out by Inspection START (10.02.2021 15:34):
//    @Transaction
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void update(CourseInstructor... pairs);
// --Commented out by Inspection STOP (10.02.2021 15:34)

// --Commented out by Inspection START (10.02.2021 15:22):
//    @Transaction
//    @Query("DELETE FROM course_instructor_table")
//    void deleteAll();
// --Commented out by Inspection STOP (10.02.2021 15:22)
}
