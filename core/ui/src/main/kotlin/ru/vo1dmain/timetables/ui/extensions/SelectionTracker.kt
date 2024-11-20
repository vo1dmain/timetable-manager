package ru.vo1dmain.timetables.ui.extensions

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView


fun <T : Any> SelectionTracker<T>.observe(
    onSelectionChanged: ((SelectionTracker<T>) -> Unit)?,
    onItemStateChanged: ((T, Boolean) -> Unit)? = null
) = addObserver(object : SelectionTracker.SelectionObserver<T>() {
    override fun onSelectionChanged() {
        onSelectionChanged?.invoke(this@observe)
    }
    
    override fun onItemStateChanged(key: T, selected: Boolean) {
        onItemStateChanged?.invoke(key, selected)
    }
})

inline fun <K : Any> selectionTracker(
    selectionId: String,
    recyclerView: RecyclerView,
    keyProvider: ItemKeyProvider<K>,
    detailsLookup: ItemDetailsLookup<K>,
    storage: StorageStrategy<K>,
    builder: SelectionTracker.Builder<K>.() -> Unit
): SelectionTracker<K> =
    SelectionTracker.Builder(selectionId, recyclerView, keyProvider, detailsLookup, storage)
        .apply(builder)
        .build()