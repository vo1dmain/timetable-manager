package ru.vo1dmain.timetables.data.entities

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Update
import androidx.room.Upsert

internal interface BaseDao<T> {
    @Insert(onConflict = IGNORE)
    suspend fun insert(item: T): Long
    
    @Insert(onConflict = IGNORE)
    suspend fun insert(items: List<T>): List<Long>
    
    @Update
    suspend fun update(item: T)
    
    @Upsert
    suspend fun upsert(item: T): Long
    
    @Delete
    suspend fun delete(vararg items: T)
}