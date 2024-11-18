package ru.vo1dmain.ttmanager.sections.instructors.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1dmain.ttmanager.data.entities.instructors.InstructorsRepository
import ru.vo1dmain.ttmanager.ui.selection.SelectableListViewModel

internal class InstructorsViewModel(application: Application) :
    SelectableListViewModel<Long>(application) {
    private val repo = InstructorsRepository(application)
    
    val all by lazy { repo.all }
    
    
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
    
    
    private suspend fun tryDeleteSelected() = try {
        repo.deleteByIds(tracker.selection.toMutableList())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
    
    private suspend fun tryDeleteAll() = try {
        repo.deleteAll()
        true
    } catch (e: Exception) {
        false
    }
}