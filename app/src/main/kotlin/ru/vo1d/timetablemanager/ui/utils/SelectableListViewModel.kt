package ru.vo1d.timetablemanager.ui.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.vo1d.timetablemanager.data.BaseDao
import ru.vo1d.timetablemanager.data.BaseRepository
import ru.vo1d.timetablemanager.data.DatabaseEntity

internal abstract class SelectableListViewModel<PK, I, D>(application: Application) :
    AndroidViewModel(application) where I : DatabaseEntity<PK>, D : BaseDao<PK, I> {
    protected abstract val repo: BaseRepository<PK, I, D>

    private val _selectedItems = MutableStateFlow(mutableListOf<I>())
    val selectedItems by lazy { _selectedItems.asStateFlow() }


    fun select(item: I) {
        val list = _selectedItems.value
        list.add(item)
        _selectedItems.update { list }
    }

    fun unselect(item: I) {
        val list = _selectedItems.value
        list.remove(item)
        _selectedItems.update { list }
    }

    fun clearSelection() {
        _selectedItems.value.clear()
    }
}