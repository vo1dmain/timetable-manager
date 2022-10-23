package ru.vo1d.timetablemanager.data.entities.instructors

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vo1d.timetablemanager.data.BaseDao

@Dao
interface InstructorsDao : BaseDao<Instructor> {

    @get:Query("SELECT * FROM instructors ORDER BY lastName ASC, middleName ASC, firstName ASC")
    val all: Flow<List<Instructor>>


    @Query(
        """SELECT * FROM instructors AS i
           INNER JOIN instructors_fts AS fts ON fts.rowId == i.id
           WHERE instructors_fts MATCH :filter
           ORDER BY lastName ASC, middleName ASC, firstName ASC"""
    )
    fun find(filter: String): Flow<List<Instructor>>

    @Transaction
    @Query(
        """SELECT i.* FROM instructors AS i
        INNER JOIN subject_instructors AS si ON i.id = si.instructorId
        WHERE si.subjectId = :subjectId
        GROUP BY i.id ORDER BY lastName ASC, middleName ASC, firstName ASC"""
    )
    fun findForSubject(subjectId: Int): Flow<List<Instructor>>


    @Query("DELETE FROM instructors")
    suspend fun deleteAll()

    @Query("SELECT * FROM instructors WHERE id = :id")
    suspend fun findById(id: Int): Instructor
}