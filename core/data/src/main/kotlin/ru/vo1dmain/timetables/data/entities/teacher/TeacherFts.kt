package ru.vo1dmain.timetables.data.entities.teacher

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Teacher::class)
@Entity(tableName = "teachers_fts")
data class TeacherFts(
    val name: String,
    val email: String
)
