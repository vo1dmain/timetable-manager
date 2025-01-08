package ru.vo1dmain.timetables.data.repos

import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.models.Subject
import ru.vo1dmain.timetables.data.sources.subject.SubjectDataSource

class SubjectRepository(private val source: SubjectDataSource) {
    fun getAll(): Flow<List<Subject>> {
        return source.getAll()
    }
    
    suspend fun create(subject: Subject): Int {
        return source.create(subject)
    }
    
    suspend fun upsert(subject: Subject): Int {
        return source.upsert(subject)
    }
    
    suspend fun findById(id: Int): Subject? {
        return source.findById(id)
    }
    
    suspend fun filter(filter: String): List<Subject> {
        return source.filter(filter)
    }
    
    suspend fun deleteByIds(ids: Collection<Int>) {
        source.deleteByIds(ids)
    }
}