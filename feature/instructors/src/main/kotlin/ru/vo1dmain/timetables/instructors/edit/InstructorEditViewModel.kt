package ru.vo1dmain.timetables.instructors.edit

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
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
    
    private val _image = mutableStateOf<String?>(null)
    
    private val id = savedStateHandle.toRoute<InstructorEdit>().id
    
    init {
        if (id != null) {
            viewModelScope.launch {
                val record = repo.findById(id)
                name.value = record.fullName
                email.value = record.email
            }
        }
    }
    
    val image: State<String?> get() = _image
    val name = mutableStateOf("")
    val email = mutableStateOf<String?>(null)
    
    val isEditMode get() = id != null
    
    val canBeSubmitted = derivedStateOf { name.value.isNotBlank() }
    
    fun submit() {
        viewModelScope.launch {
            val nameParts = name.value.split(' ')
            
            val instructor = Instructor(
                id = id ?: DatabaseEntity.DEFAULT_ID,
                firstName = nameParts.elementAtOrElse(0) { name.value },
                middleName = nameParts.elementAtOrNull(1),
                lastName = nameParts.elementAtOrNull(2),
                email = email.value?.trim(),
                image = image.value
            )
            
            tryUpsert(instructor)
        }
    }
    
    fun savePhoto(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, UUID.randomUUID().toString())
        viewModelScope.launch(Dispatchers.IO) {
            val previousImage = _image.value
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
        
        _image.value = imageFile.toUri().toString()
    }
    
    private suspend fun tryUpsert(instructor: Instructor) {
        try {
            repo.upsert(instructor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}