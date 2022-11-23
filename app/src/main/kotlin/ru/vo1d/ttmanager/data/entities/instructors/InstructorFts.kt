package ru.vo1d.ttmanager.data.entities.instructors

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Instructor::class)
@Entity(tableName = "instructors_fts")
data class InstructorFts(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String
)
