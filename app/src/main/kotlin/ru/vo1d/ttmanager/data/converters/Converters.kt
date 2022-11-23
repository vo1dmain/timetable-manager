package ru.vo1d.ttmanager.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.isoDayNumber
import ru.vo1d.ttmanager.data.entities.sessions.SessionType

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

    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek) =
        day.isoDayNumber

    @TypeConverter
    fun toDayOfWeek(isoDayNumber: Int) =
        DayOfWeek(isoDayNumber)
}