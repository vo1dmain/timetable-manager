package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseDao
import ru.vo1dmain.timetables.data.entities.event.EventType

@Dao
interface SubjectsDao : BaseDao<Subject> {
    @get:Query("SELECT * FROM subjects ORDER BY title ASC")
    val all: Flow<List<Subject>>
    
    
    @Query(
        """SELECT * FROM subjects
        WHERE title LIKE '%' || :filter || '%'
        ORDER BY title ASC"""
    )
    fun find(filter: String): Flow<List<Subject>>
    
    @Query("SELECT types FROM subjects WHERE id = :id")
    fun findTypesFor(id: Int): Flow<List<EventType>>
    
    
    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun findById(id: Int): Subject?
    
    @Query("DELETE FROM subjects")
    suspend fun deleteAll()
    
    @Query("DELETE FROM subjects WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: Collection<Int>)
}