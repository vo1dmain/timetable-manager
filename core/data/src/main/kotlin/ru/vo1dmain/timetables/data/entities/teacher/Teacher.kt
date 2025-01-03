package ru.vo1dmain.timetables.data.entities.teacher

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.DEFAULT_ID

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val name: String,
    val title: String? = null,
    val email: String? = null,
    val image: String? = null
) : DatabaseEntity