package ru.vo1dmain.timetables.data.entities.instructor

import android.app.Application
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

class InstructorsRepository(application: Application) :
    BaseRepository<Instructor, InstructorsDao>(TimetableDb.instance(application).instructorsDao()) {
    
    val all get() = dao.all
    
    fun find(query: String): Flow<List<Instructor>> {
        return dao.find(query)
    }
    
    fun findForSubject(subjectId: Int): Flow<List<Instructor>> {
        return dao.findForSubject(subjectId)
    }
    
    suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }
    
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    
    suspend fun findById(id: Int): Instructor {
        return dao.findById(id)
    }
}