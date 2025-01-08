package ru.vo1dmain.timetables.data.repos

import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.data.sources.teacher.TeacherDataSource

class TeachersRepository(private val source: TeacherDataSource) {
    fun getAll(): Flow<List<Teacher>> {
        return source.getAll()
    }
    
    suspend fun upsert(teacher: Teacher) {
        source.upsert(teacher)
    }
    
    suspend fun deleteByIds(ids: Collection<Int>) {
        source.deleteByIds(ids)
    }
    
    suspend fun findById(id: Int): Teacher? {
        return source.findById(id)
    }
}