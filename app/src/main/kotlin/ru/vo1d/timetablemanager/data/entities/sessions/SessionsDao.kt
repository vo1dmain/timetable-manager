package ru.vo1d.timetablemanager.data.entities.sessions

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import ru.vo1d.timetablemanager.data.BaseDao

@Dao
interface SessionsDao : BaseDao<Int, Session> {
    @Transaction
    @Query("SELECT * FROM sessions WHERE day = :day ORDER BY time(startTime)")
    fun findAllForDay(day: DayOfWeek): Flow<List<Session>>

    @Transaction
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun findById(id: Int): Session?
}