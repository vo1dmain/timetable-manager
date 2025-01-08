package ru.vo1dmain.timetables.data.models

data class Subject(
    val id: Int,
    val title: String,
    val types: List<EventType>,
    val teachers: List<Teacher>
)
