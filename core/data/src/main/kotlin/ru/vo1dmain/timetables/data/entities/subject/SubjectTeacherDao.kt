package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.vo1dmain.timetables.data.entities.BaseDao

@Dao
internal interface SubjectTeacherDao : BaseDao<SubjectTeacher> {
    @Query("DElETE FROM subject_teacher WHERE subjectId = :subjectId AND teacherId NOT IN (:relatedTeachersIds)")
    suspend fun deleteUnrelated(subjectId: Int, relatedTeachersIds: List<Int>)
    
    @Transaction
    suspend fun refreshFor(subjectId: Int, relatedTeachersIds: List<Int>) {
        deleteUnrelated(subjectId, relatedTeachersIds)
        val references = relatedTeachersIds.map { SubjectTeacher(subjectId, it) }
        insert(references)
    }
}