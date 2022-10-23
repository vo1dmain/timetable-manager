package ru.vo1d.timetablemanager.data.entities.weeks

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vo1d.timetablemanager.data.DatabaseEntity
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.DEFAULT_ID

@Entity(tableName = "weeks")
data class Week(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val title: String
) : DatabaseEntity
