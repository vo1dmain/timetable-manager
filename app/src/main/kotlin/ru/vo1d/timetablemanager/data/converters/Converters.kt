package ru.vo1d.timetablemanager.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime
import ru.vo1d.timetablemanager.data.entities.sessions.SessionType

class Converters {
    @TypeConverter
    fun fromDate(date: LocalTime) =
        date.toString()

    @TypeConverter
    fun toDate(string: String) =
        LocalTime.parse(string)

    @TypeConverter
    fun fromTypesList(list: List<SessionType>) =
        list.joinToString(",")

    @TypeConverter
    fun toTypesList(types: String) =
        types.split(",").map(SessionType::valueOf)
}