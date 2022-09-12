package ru.vo1d.timetablemanager.data.entities.subjectinstructors

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import ru.vo1d.timetablemanager.data.DatabaseEntity
import ru.vo1d.timetablemanager.data.entities.instructors.Instructor
import ru.vo1d.timetablemanager.data.entities.subjects.Subject

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
data class SubjectInstructor(val subjectId: Int, val instructorId: Int) : DatabaseEntity<Unit> {
    override val id = Unit
}