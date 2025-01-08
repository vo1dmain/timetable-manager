package ru.vo1dmain.timetables.data.sources.event

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.models.Event
import ru.vo1dmain.timetables.data.sources.BaseDataSource

interface EventDataSource : BaseDataSource<Event, Int> {
    fun findAllForDate(date: Instant): Flow<List<Event>>
}