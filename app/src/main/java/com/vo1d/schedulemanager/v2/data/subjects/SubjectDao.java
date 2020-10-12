package com.vo1d.schedulemanager.v2.data.subjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vo1d.schedulemanager.v2.data.IBaseDao;

import java.util.List;

@Dao
public abstract class SubjectDao implements IBaseDao<Subject> {

    @Transaction
    @Query("DELETE FROM subject_table")
    abstract void deleteAll();

    @Transaction
    @Query("SELECT * FROM subject_table ORDER BY title ASC")
    abstract LiveData<List<Subject>> getAll();

    @Transaction
    @Query("SELECT * FROM subject_table WHERE title LIKE '%' || :filter || '%' ORDER BY title ASC")
    abstract List<Subject> getFilteredSubjects(String filter);

    @Query("SELECT * FROM subject_table WHERE id=:id")
    abstract Subject findSubjectById(int id);
}
