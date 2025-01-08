package ru.vo1dmain.timetables.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.isoDayNumber
import ru.vo1dmain.timetables.data.models.EventType

class Converters {
    @TypeConverter
    fun fromInstant(instant: Instant) = instant.toString()
    
    @TypeConverter
    fun toInstant(string: String) = Instant.parse(string)
    
    @TypeConverter
    fun fromTime(time: LocalTime) = time.toString()
    
    @TypeConverter
    fun toTime(string: String) = LocalTime.parse(string)
    
    @TypeConverter
    fun fromTypesList(list: List<EventType>) = list.joinToString(",")
    
    @TypeConverter
    fun toTypesList(types: String) = types.split(",").map(EventType::valueOf)
    
    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek) = day.isoDayNumber
    
    @TypeConverter
    fun toDayOfWeek(isoDayNumber: Int) = DayOfWeek(isoDayNumber)
}