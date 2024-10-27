package ru.vo1dmain.ttmanager.data.entities.instructors

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vo1dmain.ttmanager.data.DatabaseEntity
import ru.vo1dmain.ttmanager.data.DatabaseEntity.Companion.DEFAULT_ID

@Entity(tableName = "instructors")
data class Instructor(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String
) : DatabaseEntity {
    val shortName get() = "$lastName ${firstName[0]}. ${middleName[0]}."
    val fullName get() = "$lastName $firstName $middleName"
}