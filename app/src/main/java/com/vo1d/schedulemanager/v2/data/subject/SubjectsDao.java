package com.vo1d.schedulemanager.v2.data.subject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.BaseDao;

import java.util.List;

@Dao
public abstract class SubjectsDao implements BaseDao<Subject> {

    @Transaction
    @Query("DELETE FROM subject_table")
    abstract void deleteAll();

    @Transaction
    @Query("SELECT * FROM subject_table ORDER BY title ASC")
    abstract LiveData<List<Subject>> getAllSubjects();

    @Transaction
    @Query("SELECT * FROM subject_table WHERE title LIKE '%' || :filter || '%' ORDER BY title ASC")
    abstract List<Subject> getFilteredSubjects(String filter);

    @Query("SELECT * FROM subject_table WHERE id=:id")
    abstract Subject findSubjectById(int id);
}
