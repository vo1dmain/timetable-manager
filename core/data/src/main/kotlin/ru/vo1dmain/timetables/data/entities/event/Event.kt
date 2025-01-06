package ru.vo1dmain.timetables.data.entities.event

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.teacher.Teacher

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = Teacher::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["subjectId"]),
        Index(value = ["teacherId"]),
    ]
)
data class Event(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val subjectId: Int,
    val teacherId: Int,
    val place: String,
    val type: EventType,
    val date: Instant? = null,
    val startTime: Instant,
    val endTime: Instant
) : DatabaseEntity