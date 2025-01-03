package ru.vo1dmain.timetables.data.entities.session

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.teacher.Teacher
import ru.vo1dmain.timetables.data.entities.week.Week

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
            entity = Teacher::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
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
        Index(value = ["teacherId"]),
        Index(value = ["weekId"])
    ]
)
data class Session(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val subjectId: Int,
    val teacherId: Int,
    val weekId: Int,
    val day: DayOfWeek,
    val place: String,
    val type: SessionType,
    val startTime: LocalTime,
    val endTime: LocalTime
) : DatabaseEntity