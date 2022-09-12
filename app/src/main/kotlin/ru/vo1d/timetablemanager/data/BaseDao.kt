package ru.vo1d.timetablemanager.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Update

interface BaseDao<PK, T : DatabaseEntity<PK>> {
    @Insert(onConflict = IGNORE)
    suspend fun insert(items: T): PK

    @Insert(onConflict = IGNORE)
    suspend fun insert(vararg items: T)

    @Update
    suspend fun update(item: T)

    @Delete
    suspend fun delete(vararg items: T)
}