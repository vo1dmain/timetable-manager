package ru.vo1dmain.timetables.teachers.edit

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.DEFAULT_ID
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.data.repos.TeachersRepository
import ru.vo1dmain.timetables.data.sources.teacher.TeacherRoomDataSource
import ru.vo1dmain.timetables.teachers.TeacherEdit
import java.io.File
import java.util.UUID

internal class TeacherEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = TeachersRepository(TeacherRoomDataSource(application))
    
    private val id = savedStateHandle.toRoute<TeacherEdit>().id
    private val image = mutableStateOf<String?>(null)
    
    val state = EditScreenState(image = image)
    
    val isCreationMode get() = id == null
    
    init {
        if (id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val model = repo.findById(id) ?: return@launch
                state.name.setTextAndPlaceCursorAtEnd(model.name)
                state.title.setTextAndPlaceCursorAtEnd(model.title ?: "")
                state.email.setTextAndPlaceCursorAtEnd(model.email ?: "")
                image.value = model.image
            }
        }
    }
    
    fun submit() {
        viewModelScope.launch(Dispatchers.IO) {
            val teacher = Teacher(
                id = id ?: DEFAULT_ID,
                name = state.name.text.toString().trim(),
                email = state.email.text.toString().trim(),
                title = state.title.text.toString().trim(),
                image = state.image
            )
            
            repo.upsert(teacher)
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
        
        image.value = imageFile.toUri().toString()
    }
}

@Stable
internal class EditScreenState(
    image: State<String?> = mutableStateOf(null),
    val name: TextFieldState = TextFieldState(),
    val title: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState()
) {
    val image by image
    
    val canBeSubmitted by derivedStateOf { name.text.isNotBlank() }
}