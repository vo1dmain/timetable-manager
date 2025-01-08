package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import ru.vo1dmain.timetables.data.entities.teacher.TeacherEntity

@Entity(
    tableName = "subject_teacher",
    primaryKeys = ["subjectId", "teacherId"],
    indices = [Index(value = ["subjectId"]), Index(value = ["teacherId"])],
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ), ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
internal data class SubjectTeacher(
    val subjectId: Int,
    val teacherId: Int
)