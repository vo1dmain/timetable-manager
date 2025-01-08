package ru.vo1dmain.timetables.data.sources.subject

import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.models.Subject
import ru.vo1dmain.timetables.data.sources.BaseDataSource

interface SubjectDataSource : BaseDataSource<Subject, Int> {
    fun getAll(): Flow<List<Subject>>
    suspend fun filter(filter: String): List<Subject>
    suspend fun deleteByIds(ids: Collection<Int>)
}