package ru.vo1dmain.timetables.data.sources.subject

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import ru.vo1dmain.timetables.data.TimetableDb
import ru.vo1dmain.timetables.data.entities.subject.SubjectDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacher
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacherDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectWithTeachers
import ru.vo1dmain.timetables.data.models.Subject
import ru.vo1dmain.timetables.data.toEntity
import ru.vo1dmain.timetables.data.toModel

class SubjectRoomDataSource internal constructor(
    private val dao: SubjectDao,
    private val junctionDao: SubjectTeacherDao
) : SubjectDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<Subject>> {
        return dao.getAll().mapLatest { it.map(SubjectWithTeachers::toModel) }
    }
    
    override suspend fun filter(filter: String): List<Subject> {
        return dao.filter(filter).map(SubjectWithTeachers::toModel)
    }
    
    override suspend fun deleteByIds(ids: Collection<Int>) {
        dao.deleteByIds(ids)
    }
    
    override suspend fun create(item: Subject): Int {
        val id = dao.insert(item.toEntity()).toInt()
        val references = item.teachers.map { SubjectTeacher(id, it.id) }
        junctionDao.insert(references)
        
        return id
    }
    
    override suspend fun findById(id: Int): Subject? {
        return dao.findById(id)?.toModel()
    }
    
    override suspend fun update(item: Subject) {
        dao.update(item.toEntity())
        val relatedTeachersIds = item.teachers.map { it.id }
        junctionDao.refreshFor(item.id, relatedTeachersIds)
    }
    
    override suspend fun upsert(item: Subject): Int {
        return dao.upsert(item.toEntity()).toInt()
    }
    
    override suspend fun delete(item: Subject) {
        dao.delete(item.toEntity())
    }
    
    companion object {
        fun instance(application: Application): SubjectDataSource {
            val db = TimetableDb.instance(application)
            return SubjectRoomDataSource(
                dao = db.subjectDao(),
                junctionDao = db.subjectTeacherDao()
            )
        }
    }
}