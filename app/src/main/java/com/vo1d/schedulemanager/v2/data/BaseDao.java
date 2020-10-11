package com.vo1d.schedulemanager.v2.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {
    @Insert
    long insert(T obj);

    @Update
    void update(T obj);

    @Delete
    void delete(T... objects);
}
