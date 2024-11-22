package ru.vo1dmain.timetables.data.entities.instructor

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseDao

@Dao
interface InstructorsDao : BaseDao<Instructor> {
    
    @get:Query("SELECT * FROM instructors ORDER BY lastName ASC, firstName ASC, middleName ASC")
    val all: Flow<List<Instructor>>
    
    
    @Query(
        """SELECT * FROM instructors AS i
           INNER JOIN instructors_fts AS fts ON fts.rowId == i.id
           WHERE instructors_fts MATCH :query
           ORDER BY lastName ASC, firstName ASC, middleName ASC"""
    )
    fun find(query: String): Flow<List<Instructor>>
    
    @Transaction
    @Query(
        """SELECT i.* FROM instructors AS i
        INNER JOIN subject_instructors AS si ON i.id = si.instructorId
        WHERE si.subjectId = :subjectId
        GROUP BY i.id ORDER BY lastName ASC, firstName ASC, middleName ASC"""
    )
    fun findForSubject(subjectId: Int): Flow<List<Instructor>>
    
    
    @Query("DELETE FROM instructors")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM instructors WHERE id = :id")
    suspend fun findById(id: Int): Instructor
    
    @Query("DELETE FROM instructors WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
}