package ru.vo1d.ttmanager.data.entities.subjectinstructors

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import ru.vo1d.ttmanager.data.DatabaseEntity
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.subjects.Subject

@Entity(
    tableName = "subject_instructors",
    primaryKeys = ["subjectId", "instructorId"],
    indices = [Index(value = ["subjectId"]), Index(value = ["instructorId"])],
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ), ForeignKey(
            entity = Instructor::class,
            parentColumns = ["id"],
            childColumns = ["instructorId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class SubjectInstructor(val subjectId: Int, val instructorId: Int) : DatabaseEntity