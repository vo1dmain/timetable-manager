package ru.vo1d.timetablemanager.data.entities.subjects

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vo1d.timetablemanager.data.BaseDao
import ru.vo1d.timetablemanager.data.entities.sessions.SessionType

@Dao
interface SubjectsDao : BaseDao<Int, Subject> {
    @get:Query("SELECT * FROM subjects ORDER BY title ASC")
    val all: Flow<List<Subject>>


    @Query(
        """SELECT * FROM subjects
        WHERE title LIKE '%' || :filter || '%'
        ORDER BY title ASC"""
    )
    fun find(filter: String): Flow<List<Subject>>

    @Query("SELECT types FROM subjects WHERE id = :id")
    fun findTypesFor(id: Int): Flow<List<SessionType>>


    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun findById(id: Int): Subject?

    @Query("DELETE FROM subjects")
    suspend fun deleteAll()
}