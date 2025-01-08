package ru.vo1dmain.timetables.data.entities.teacher

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.entities.BaseDao

@Dao
internal interface TeacherDao : BaseDao<TeacherEntity> {
    @Query("SELECT * FROM teachers ORDER BY name ASC")
    fun getAll(): Flow<List<TeacherEntity>>
    
    @Query("SELECT * FROM teachers WHERE id = :id")
    suspend fun findById(id: Int): TeacherEntity?
    
    @Query("DELETE FROM teachers WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: Collection<Int>)
}