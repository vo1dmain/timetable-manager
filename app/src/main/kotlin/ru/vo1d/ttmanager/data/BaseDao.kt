package ru.vo1d.ttmanager.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Update

interface BaseDao<T : DatabaseEntity> {
    @Insert(onConflict = IGNORE)
    suspend fun insert(item: T): Long
    
    @Insert(onConflict = IGNORE)
    suspend fun insert(vararg items: T)
    
    @Update
    suspend fun update(item: T)
    
    @Delete
    suspend fun delete(vararg items: T)
}