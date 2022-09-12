package ru.vo1d.timetablemanager.data.entities.subjects

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.vo1d.timetablemanager.data.DatabaseEntity
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1d.timetablemanager.data.entities.sessions.SessionType

@Entity(tableName = "subjects", indices = [Index(value = ["id"], unique = true)])
data class Subject(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = DEFAULT_ID,
    val title: String,
    val types: List<SessionType>
) : DatabaseEntity<Int> {
    override fun toString() = title
}