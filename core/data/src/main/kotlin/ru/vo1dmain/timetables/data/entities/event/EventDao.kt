package ru.vo1dmain.timetables.data.entities.event

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.entities.BaseDao

@Dao
internal interface EventDao : BaseDao<EventEntity> {
    @Query("SELECT * FROM events WHERE date = :date ORDER BY time(startTime)")
    fun findAllForDate(date: Instant): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun findById(id: Int): EventEntity?
}