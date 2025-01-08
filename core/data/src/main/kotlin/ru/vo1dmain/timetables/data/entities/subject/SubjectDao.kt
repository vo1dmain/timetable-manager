package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.entities.BaseDao

@Dao
internal interface SubjectDao : BaseDao<SubjectEntity> {
    @Transaction
    @Query("SELECT * FROM subjects")
    fun getAll(): Flow<List<SubjectWithTeachers>>
    
    @Transaction
    @Query(
        """SELECT * FROM subjects
        WHERE title LIKE '%' || :filter || '%'
        ORDER BY title ASC"""
    )
    suspend fun filter(filter: String): List<SubjectWithTeachers>
    
    @Transaction
    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun findById(id: Int): SubjectWithTeachers?
    
    @Query("DELETE FROM subjects WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: Collection<Int>)
}