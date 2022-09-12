package ru.vo1d.timetablemanager.data

interface DatabaseEntity<PK> {
    val id: PK

    companion object {
        const val DEFAULT_ID = 0
        const val INVALID_ID = -1
    }
}