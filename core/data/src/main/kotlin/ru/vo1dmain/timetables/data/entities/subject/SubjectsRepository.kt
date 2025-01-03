package ru.vo1dmain.timetables.data.entities.subject

import android.app.Application
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb
import ru.vo1dmain.timetables.data.entities.session.SessionType

class SubjectsRepository(application: Application) :
    BaseRepository<Subject, SubjectsDao>(TimetableDb.instance(application).subjectsDao()) {
    
    private val pairDao = TimetableDb.instance(application).subjectTeacherDao()
    
    val all get() = dao.all
    
    
    fun find(filter: String): Flow<List<Subject>> {
        return dao.find(filter)
    }
    
    fun findTypesFor(id: Int): Flow<List<SessionType>> {
        return dao.findTypesFor(id)
    }
    
    
    suspend fun insertPair(pair: SubjectTeacher): Long {
        return pairDao.insert(pair)
    }
    
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    
    suspend fun findById(id: Int): Subject? {
        return dao.findById(id)
    }
}