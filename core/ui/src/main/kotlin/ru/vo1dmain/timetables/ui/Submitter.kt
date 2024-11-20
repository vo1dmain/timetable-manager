package ru.vo1dmain.timetables.ui

interface Submitter {
    fun submit(onResult: (Boolean) -> Unit)
}