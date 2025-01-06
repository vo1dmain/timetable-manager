package ru.vo1dmain.timetables.calendar.events.edit

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.IntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1dmain.timetables.data.entities.event.Event
import ru.vo1dmain.timetables.data.entities.event.EventType
import ru.vo1dmain.timetables.data.entities.event.EventsRepository
import ru.vo1dmain.timetables.data.entities.subject.SubjectsRepository
import ru.vo1dmain.timetables.data.entities.teacher.TeachersRepository
import kotlin.time.Duration.Companion.minutes

internal class EventEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val eventsRepo = EventsRepository(application)
    private val subjectsRepo = SubjectsRepository(application)
    private val teachersRepo = TeachersRepository(application)
    
    private val id = savedStateHandle.toRoute<EventEdit>().id
    
    val state = EditScreenState()
    
    val subjects by lazy {
        subjectsRepo.all.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }
    
    fun submit() {
        viewModelScope.launch {
            val event = Event(
                id = id ?: INVALID_ID,
                subjectId = state.subjectId,
                teacherId = state.teacherId,
                place = state.place.text.toString().trim(),
                type = state.type,
                startTime = state.startTime,
                endTime = state.endTime
            )
            trySubmit(event)
        }
    }
    
    private suspend fun trySubmit(event: Event) {
        try {
            eventsRepo.insert(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Stable
internal class EditScreenState(
    subjectId: IntState = mutableIntStateOf(INVALID_ID),
    teacherId: IntState = mutableIntStateOf(INVALID_ID),
    type: State<EventType> = mutableStateOf(EventType.None),
    startTime: State<Instant> = mutableStateOf(Clock.System.now()),
    endTime: State<Instant> = mutableStateOf(startTime.value + 45.minutes),
    val place: TextFieldState = TextFieldState()
) {
    val subjectId by subjectId
    val teacherId by teacherId
    val type by type
    val startTime by startTime
    val endTime by endTime
    
    val canBeSubmitted by derivedStateOf {
        this.subjectId != INVALID_ID
                && this.teacherId != INVALID_ID
                && this.type != EventType.None
                && place.text.isNotBlank()
    }
}