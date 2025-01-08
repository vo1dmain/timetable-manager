package ru.vo1dmain.timetables.data.models

import kotlinx.datetime.Instant

data class Event(
    val id: Int,
    val subjectId: Int,
    val hostId: Int,
    val place: String,
    val type: EventType,
    val date: Instant? = null,
    val startTime: Instant,
    val endTime: Instant
)

enum class EventType {
    None,
    Lecture,
    Practice,
    Laboratory
}