package ru.vo1d.ttmanager.ui.common.selection

import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.properties.Delegates

abstract class SelectableViewHolder<T : Any>(itemView: View) : ViewHolder(itemView) {
    protected abstract var bindItemId: T
    
    var isSelected by Delegates.observable(false) { _, oldValue, newValue ->
        if (newValue == oldValue) return@observable
        onSelectionChanged(newValue)
    }
    
    fun getItemDetails() = object : ItemDetailsLookup.ItemDetails<T>() {
        override fun getPosition() = adapterPosition
        override fun getSelectionKey() = bindItemId
    }
    
    
    protected abstract fun onSelectionChanged(selected: Boolean)
}