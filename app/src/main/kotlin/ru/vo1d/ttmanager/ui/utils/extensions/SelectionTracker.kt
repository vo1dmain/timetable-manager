package ru.vo1d.ttmanager.ui.utils.extensions

import androidx.recyclerview.selection.SelectionTracker


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