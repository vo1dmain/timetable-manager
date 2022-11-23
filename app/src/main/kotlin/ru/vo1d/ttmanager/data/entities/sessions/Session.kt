package ru.vo1d.ttmanager.data.entities.sessions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import ru.vo1d.ttmanager.data.DatabaseEntity
import ru.vo1d.ttmanager.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.subjects.Subject
import ru.vo1d.ttmanager.data.entities.weeks.Week

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = Instructor::class,
            parentColumns = ["id"],
            childColumns = ["instructorId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = Week::class,
            parentColumns = ["id"],
            childColumns = ["weekId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["subjectId"]),
        Index(value = ["instructorId"]),
        Index(value = ["weekId"])
    ]
)
data class Session(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val subjectId: Int,
    val instructorId: Int,
    val weekId: Int,
    val day: DayOfWeek,
    val place: String,
    val type: SessionType,
    val startTime: LocalTime,
    val endTime: LocalTime
) : DatabaseEntity