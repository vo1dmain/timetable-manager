package ru.vo1d.ttmanager.data.entities.sessions

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import ru.vo1d.ttmanager.data.BaseDao

@Dao
interface SessionsDao : BaseDao<Session> {
    @Query("SELECT DISTINCT day FROM sessions WHERE weekId = :weekId")
    fun findAllDaysForWeek(weekId: Int): Flow<List<DayOfWeek>>

    @Query("SELECT * FROM sessions WHERE day = :day ORDER BY time(startTime)")
    fun findAllForDay(day: DayOfWeek): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun findById(id: Int): Session?
}