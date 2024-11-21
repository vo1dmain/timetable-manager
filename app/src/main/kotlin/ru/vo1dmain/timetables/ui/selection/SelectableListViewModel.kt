package ru.vo1dmain.timetables.ui.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.selection.SelectionTracker

abstract class SelectableListViewModel<K : Any>(application: Application) : AndroidViewModel(application) {
    
    lateinit var tracker: SelectionTracker<K>
}