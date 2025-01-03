package ru.vo1dmain.timetables.instructors.edit

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.instructor.Instructor
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsRepository
import java.io.File
import java.util.UUID

internal class InstructorEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = InstructorsRepository(application)
    
    private val id = savedStateHandle.toRoute<InstructorEdit>().id
    
    
    val state = EditScreenState()
    
    val isEditMode get() = id != null
    
    init {
        if (id != null) {
            viewModelScope.launch {
                val record = repo.findById(id)
                state.name.setTextAndPlaceCursorAtEnd(record.name)
                state.title.setTextAndPlaceCursorAtEnd(record.title ?: "")
                state.email.setTextAndPlaceCursorAtEnd(record.email ?: "")
                state.image = record.image
            }
        }
    }
    
    fun submit() {
        viewModelScope.launch {
            val instructor = Instructor(
                id = id ?: DatabaseEntity.DEFAULT_ID,
                name = state.name.text.toString().trim(),
                email = state.email.text.toString().trim(),
                title = state.title.text.toString().trim(),
                image = state.image
            )
            
            tryUpsert(instructor)
        }
    }
    
    fun savePhoto(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, UUID.randomUUID().toString())
        viewModelScope.launch(Dispatchers.IO) {
            val previousImage = state.image
            if (previousImage != null) {
                context.deleteFile(previousImage)
            }
            
            val data = context.contentResolver.openInputStream(uri).use {
                it?.readBytes()
            }
            
            context.openFileOutput(imageFile.path, Context.MODE_PRIVATE).use {
                it.write(data)
            }
        }
        
        state.image = imageFile.toUri().toString()
    }
    
    private suspend fun tryUpsert(instructor: Instructor) {
        try {
            repo.upsert(instructor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Stable
internal class EditScreenState(
    image: MutableState<String?> = mutableStateOf(null),
    val name: TextFieldState = TextFieldState(),
    val title: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState()
) {
    val canBeSubmitted by derivedStateOf { this.name.text.isNotBlank() }
    
    var image by image
        internal set
}