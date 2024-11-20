package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.instructor.Instructor

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