package ru.vo1dmain.timetables.data.models

data class Teacher(
    val id: Int,
    val name: String,
    val title: String? = null,
    val email: String? = null,
    val image: String? = null
)
