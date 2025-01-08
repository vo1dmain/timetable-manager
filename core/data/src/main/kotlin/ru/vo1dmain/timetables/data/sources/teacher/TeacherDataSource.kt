package ru.vo1dmain.timetables.data.sources.teacher

import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.data.sources.BaseDataSource

interface TeacherDataSource : BaseDataSource<Teacher, Int> {
    fun getAll(): Flow<List<Teacher>>
    suspend fun deleteByIds(ids: Collection<Int>)
}