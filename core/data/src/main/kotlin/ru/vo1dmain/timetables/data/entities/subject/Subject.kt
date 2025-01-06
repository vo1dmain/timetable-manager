package ru.vo1dmain.timetables.data.entities.subject

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1dmain.timetables.data.entities.event.EventType

@Entity(tableName = "subjects", indices = [Index(value = ["id"], unique = true)])
data class Subject(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val title: String,
    val types: List<EventType>
) : DatabaseEntity {
    override fun toString() = title
}