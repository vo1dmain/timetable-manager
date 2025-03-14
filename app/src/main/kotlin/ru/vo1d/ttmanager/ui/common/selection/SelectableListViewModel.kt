package ru.vo1d.ttmanager.ui.common.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.selection.SelectionTracker

internal abstract class SelectableListViewModel<K : Any>(application: Application) :
    AndroidViewModel(application) {
    
    lateinit var tracker: SelectionTracker<K>
}