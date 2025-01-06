package ru.vo1dmain.timetables.data.entities.teacher

import android.app.Application
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

class TeachersRepository(application: Application) :
    BaseRepository<Teacher, TeachersDao>(TimetableDb.instance(application).teachersDao()) {
    
    val all get() = dao.all
    
    fun find(query: String): Flow<List<Teacher>> {
        return dao.find(query)
    }
    
    fun findForSubject(subjectId: Int): Flow<List<Teacher>> {
        return dao.findForSubject(subjectId)
    }
    
    suspend fun deleteByIds(ids: Collection<Int>) {
        dao.deleteByIds(ids)
    }
    
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    
    suspend fun findById(id: Int): Teacher {
        return dao.findById(id)
    }
}