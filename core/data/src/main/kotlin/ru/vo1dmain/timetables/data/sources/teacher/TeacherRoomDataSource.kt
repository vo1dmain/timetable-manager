package ru.vo1dmain.timetables.data.sources.teacher

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import ru.vo1dmain.timetables.data.TimetableDb
import ru.vo1dmain.timetables.data.entities.teacher.TeacherDao
import ru.vo1dmain.timetables.data.entities.teacher.TeacherEntity
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.data.toEntity
import ru.vo1dmain.timetables.data.toModel

internal class TeacherRoomDataSource(private val dao: TeacherDao) : TeacherDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<Teacher>> {
        return dao.getAll().mapLatest { it.map(TeacherEntity::toModel) }
    }
    
    override suspend fun deleteByIds(ids: Collection<Int>) {
        dao.deleteByIds(ids)
    }
    
    override suspend fun create(item: Teacher): Int {
        return dao.insert(item.toEntity()).toInt()
    }
    
    override suspend fun findById(id: Int): Teacher? {
        return dao.findById(id)?.toModel()
    }
    
    override suspend fun update(item: Teacher) {
        dao.update(item.toEntity())
    }
    
    override suspend fun upsert(item: Teacher): Int {
        return dao.upsert(item.toEntity()).toInt()
    }
    
    override suspend fun delete(item: Teacher) {
        dao.delete(item.toEntity())
    }
}

@Suppress("FunctionName")
fun TeacherRoomDataSource(application: Application): TeacherDataSource {
    return TeacherRoomDataSource(TimetableDb.instance(application).teacherDao())
}