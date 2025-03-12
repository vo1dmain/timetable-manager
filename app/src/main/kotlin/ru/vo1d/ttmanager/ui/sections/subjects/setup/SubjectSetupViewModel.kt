package ru.vo1d.ttmanager.ui.sections.subjects.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.sessions.SessionType
import ru.vo1d.ttmanager.data.entities.subjectinstructors.SubjectInstructor
import ru.vo1d.ttmanager.data.entities.subjects.Subject
import ru.vo1d.ttmanager.data.entities.subjects.SubjectsRepository
import ru.vo1d.ttmanager.ui.common.Submitter

@OptIn(ExperimentalCoroutinesApi::class)
internal class SubjectSetupViewModel(application: Application) :
    AndroidViewModel(application), Submitter {
    
    private val repo = SubjectsRepository(application)
    
    private val title = MutableStateFlow("")
    private val selectedInstructors = MutableStateFlow(mutableListOf<Instructor>())
    private val selectedTypes = MutableStateFlow(mutableListOf<SessionType>())
    
    private val titleIsSet by lazy { title.mapLatest(String::isNotBlank) }
    private val instructorsAreSet by lazy { selectedInstructors.mapLatest(MutableList<Instructor>::isNotEmpty) }
    private val typesAreSet by lazy { selectedTypes.mapLatest(MutableList<SessionType>::isNotEmpty) }
    
    
    val canBeSubmitted = combine(titleIsSet, instructorsAreSet, typesAreSet) { states ->
        states.all { it }
    }
    
    override fun submit(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = trySubmit()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    fun setTitle(value: String) =
        title.update { value }
    
    fun selectType(type: SessionType) =
        selectedTypes.update { it.apply { add(type) } }
    
    fun unselectType(type: SessionType) =
        selectedTypes.update { it.apply { remove(type) } }
    
    fun selectInstructor(instructor: Instructor) =
        selectedInstructors.update { it.apply { add(instructor) } }
    
    fun unselectInstructor(instructor: Instructor) =
        selectedInstructors.update { it.apply { remove(instructor) } }
    
    
    private suspend fun trySubmit(): Boolean {
        return try {
            val item = Subject(
                title = title.value.trim(),
                types = selectedTypes.value
            )
            val id = repo.insert(item).toInt()
            
            if (id == -1) return false
            
            selectedInstructors.value.forEach {
                val pair = SubjectInstructor(id, it.id)
                repo.insertPair(pair)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}