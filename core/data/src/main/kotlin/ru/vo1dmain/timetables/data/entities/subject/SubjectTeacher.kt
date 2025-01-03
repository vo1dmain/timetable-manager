package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.teacher.Teacher

@Entity(
    tableName = "subject_teacher",
    primaryKeys = ["subjectId", "teacherId"],
    indices = [Index(value = ["subjectId"]), Index(value = ["teacherId"])],
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ), ForeignKey(
            entity = Teacher::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class SubjectTeacher(val subjectId: Int, val teacherId: Int) : DatabaseEntity