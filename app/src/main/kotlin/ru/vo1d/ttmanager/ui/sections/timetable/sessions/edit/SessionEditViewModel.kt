package ru.vo1d.ttmanager.ui.sections.timetable.sessions.edit

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vo1d.ttmanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1d.ttmanager.ui.sections.timetable.sessions.setup.SessionSetupViewModel

class SessionEditViewModel(application: Application) : SessionSetupViewModel(application) {
    private val itemId = MutableStateFlow(INVALID_ID)


    val subjectId by lazy { _subjectId.asStateFlow() }
    val instructorId by lazy { _instructorId.asStateFlow() }
    val selectedType by lazy { _selectedType.asStateFlow() }


    override fun submit() {
        viewModelScope.launch {
            val item = buildItem(itemId.value)
            sessionsRepo.update(item)
        }
    }


    fun setItemId(id: Int) =
        itemId.update { id }


    init {
        itemId.onEach {
            val item = sessionsRepo.findById(it) ?: return@onEach
            setSubjectId(item.subjectId)
            setInstructorId(item.instructorId)
            setType(item.type)
            setStartTime(item.startTime)
            setEndTime(item.endTime)
        }.launchIn(viewModelScope)
    }
}