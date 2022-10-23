package ru.vo1d.timetablemanager.data.entities.subjects

import android.app.Application
import ru.vo1d.timetablemanager.data.BaseRepository
import ru.vo1d.timetablemanager.data.TimetableDb

class SubjectsRepository(application: Application) : BaseRepository<Subject, SubjectsDao>() {
    override val dao = TimetableDb.instance(application).subjectsDao()

    val all by lazy { dao.all }


    fun find(filter: String) =
        dao.find(filter)

    fun findTypesFor(id: Int) =
        dao.findTypesFor(id)


    suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun findById(id: Int) =
        dao.findById(id)
}