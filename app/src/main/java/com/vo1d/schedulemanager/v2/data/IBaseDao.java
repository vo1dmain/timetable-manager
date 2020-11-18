package com.vo1d.schedulemanager.v2.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Transaction;
import androidx.room.Update;

public interface IBaseDao<T extends IMyEntity> {
    @Transaction
    @Insert
    void insert(T[] obj);

    @Insert
    long insert(T obj);

    @Update
    void update(T obj);

    @Transaction
    @Delete
    void delete(T[] objects);
}
