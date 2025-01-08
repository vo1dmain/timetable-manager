package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.vo1dmain.timetables.data.DEFAULT_ID
import ru.vo1dmain.timetables.data.entities.teacher.TeacherEntity
import ru.vo1dmain.timetables.data.models.EventType

@Entity(tableName = "subjects", indices = [Index(value = ["id"], unique = true)])
internal data class SubjectEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val title: String,
    val types: List<EventType>
)

internal data class SubjectWithTeachers(
    @Embedded val subject: SubjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SubjectTeacher::class,
            parentColumn = "subjectId",
            entityColumn = "teacherId"
        )
    )
    val teachers: List<TeacherEntity>
)