package ru.vo1d.timetablemanager.data.entities.instructors

import android.app.Application
import ru.vo1d.timetablemanager.data.BaseRepository
import ru.vo1d.timetablemanager.data.TimetableDb

class InstructorsRepository(application: Application) :
    BaseRepository<Instructor, InstructorsDao>() {
    override val dao = TimetableDb.instance(application).instructorsDao()

    val all get() = dao.all


    fun getFiltered(filter: String) =
        dao.getFiltered(filter)

    fun findForSubject(subjectId: Int) =
        dao.findForSubject(subjectId)


    suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun findById(id: Int) =
        dao.findById(id)
}