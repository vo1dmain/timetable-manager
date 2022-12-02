package ru.vo1d.ttmanager.data.entities.instructors

import android.app.Application
import ru.vo1d.ttmanager.data.BaseRepository
import ru.vo1d.ttmanager.data.TimetableDb

class InstructorsRepository(application: Application) :
    BaseRepository<Instructor, InstructorsDao>() {
    override val dao = TimetableDb.instance(application).instructorsDao()

    val all get() = dao.all


    fun find(query: String) =
        dao.find(query)

    fun findForSubject(subjectId: Int) =
        dao.findForSubject(subjectId)

    suspend fun deleteByIds(ids: List<Long>) =
        dao.deleteByIds(ids)

    suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun findById(id: Int) =
        dao.findById(id)
}