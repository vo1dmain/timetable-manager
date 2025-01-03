package ru.vo1dmain.timetables.data.entities.instructor

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.DEFAULT_ID

@Entity(tableName = "instructors")
data class Instructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val name: String,
    val title: String? = null,
    val email: String? = null,
    val image: String? = null
) : DatabaseEntity