package ru.vo1d.ttmanager.data.entities.subjects

import android.app.Application
import ru.vo1d.ttmanager.data.BaseRepository
import ru.vo1d.ttmanager.data.TimetableDb
import ru.vo1d.ttmanager.data.entities.subjectinstructors.SubjectInstructor

class SubjectsRepository(application: Application) : BaseRepository<Subject, SubjectsDao>() {
    override val dao = TimetableDb.instance(application).subjectsDao()
    private val pairDao = TimetableDb.instance(application).subjectInstructorsDao()
    
    val all by lazy { dao.all }
    
    
    fun find(filter: String) =
        dao.find(filter)
    
    fun findTypesFor(id: Int) =
        dao.findTypesFor(id)
    
    
    suspend fun insertPair(pair: SubjectInstructor) =
        pairDao.insert(pair)
    
    suspend fun deleteAll() =
        dao.deleteAll()
    
    suspend fun findById(id: Int) =
        dao.findById(id)
}