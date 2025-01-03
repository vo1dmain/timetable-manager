package ru.vo1dmain.timetables.data.entities.teacher

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseDao

@Dao
interface TeachersDao : BaseDao<Teacher> {
    
    @get:Query("SELECT * FROM teachers ORDER BY name ASC")
    val all: Flow<List<Teacher>>
    
    
    @Query(
        """SELECT * FROM teachers AS i
           INNER JOIN teachers_fts AS fts ON fts.rowId == i.id
           WHERE teachers_fts MATCH :query
           ORDER BY name ASC"""
    )
    fun find(query: String): Flow<List<Teacher>>
    
    @Transaction
    @Query(
        """SELECT t.* FROM teachers AS t
        INNER JOIN subject_teacher AS si ON t.id = si.teacherId
        WHERE si.subjectId = :subjectId
        GROUP BY t.id ORDER BY name ASC"""
    )
    fun findForSubject(subjectId: Int): Flow<List<Teacher>>
    
    
    @Query("DELETE FROM teachers")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM teachers WHERE id = :id")
    suspend fun findById(id: Int): Teacher
    
    @Query("DELETE FROM teachers WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
}