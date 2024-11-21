package ru.vo1dmain.timetables.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<T : DatabaseEntity> {
    @Insert(onConflict = IGNORE)
    suspend fun insert(item: T): Long
    
    @Insert(onConflict = IGNORE)
    suspend fun insert(vararg items: T)
    
    @Update
    suspend fun update(item: T)
    
    @Upsert
    suspend fun upsert(item: T)
    
    @Delete
    suspend fun delete(vararg items: T)
}