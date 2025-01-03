package ru.vo1dmain.timetables.data.entities.instructor

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Instructor::class)
@Entity(tableName = "instructors_fts")
data class InstructorFts(
    val name: String,
    val email: String
)
