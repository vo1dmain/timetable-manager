package ru.vo1d.ttmanager.ui.sections.timetable.sessions.edit

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1d.ttmanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1d.ttmanager.ui.sections.timetable.sessions.setup.SessionSetupViewModel

class SessionEditViewModel(application: Application) : SessionSetupViewModel(application) {
    private val itemId = MutableStateFlow(INVALID_ID)
    
    
    val subjectId by lazy { _subjectId.asStateFlow() }
    val instructorId by lazy { _instructorId.asStateFlow() }
    val selectedType by lazy { _selectedType.asStateFlow() }
    
    
    override fun submit(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = trySubmit()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    
    fun setItemId(id: Int) =
        itemId.update { id }
    
    
    private suspend fun trySubmit() = try {
        val item = buildItem(itemId.value)
        sessionsRepo.update(item)
        true
    } catch (e: Exception) {
        false
    }
    
    
    init {
        viewModelScope.launch {
            itemId.collectLatest {
                val item = sessionsRepo.findById(it) ?: return@collectLatest
                setSubjectId(item.subjectId)
                setInstructorId(item.instructorId)
                setType(item.type)
                setStartTime(item.startTime)
                setEndTime(item.endTime)
            }
        }
    }
}