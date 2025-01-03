package ru.vo1dmain.timetables.teachers.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1dmain.timetables.data.entities.teacher.TeachersRepository

internal class TeachersListViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repo = TeachersRepository(application)
    
    private val selected = mutableListOf<Int>()
    
    val all by lazy {
        repo.all.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )
    }
    
    fun deleteSelected(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = tryDeleteSelected()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    fun deleteAll(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = tryDeleteAll()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    private suspend fun tryDeleteSelected(): Boolean {
        return try {
            repo.deleteByIds(selected)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private suspend fun tryDeleteAll(): Boolean {
        return try {
            repo.deleteAll()
            true
        } catch (e: Exception) {
            false
        }
    }
}