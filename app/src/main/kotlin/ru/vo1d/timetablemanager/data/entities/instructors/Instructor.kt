package ru.vo1d.timetablemanager.data.entities.instructors

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.vo1d.timetablemanager.data.DatabaseEntity
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.DEFAULT_ID

@Entity(tableName = "instructors", indices = [Index(value = ["id"], unique = true)])
data class Instructor(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = DEFAULT_ID,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String
) : DatabaseEntity<Int> {
    val shortName get() = "$lastName ${firstName[0]}. ${middleName[0]}."
    val fullName get() = "$lastName $firstName $middleName"
}