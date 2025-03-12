package ru.vo1d.ttmanager.ui.common.selection

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class SelectableListAdapter<K, I, VH>(diffCallback: DiffUtil.ItemCallback<I>) :
    ListAdapter<I, VH>(diffCallback) where  K : Any, VH : SelectableViewHolder<K> {
    lateinit var tracker: SelectionTracker<K>
    
    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        if (SelectionTracker.SELECTION_CHANGED_MARKER in payloads) {
            holder.isSelected = tracker.isSelected(getItemKey(position))
            return
        }
        
        onBindViewHolder(holder, position)
    }
    
    abstract fun getItemKey(position: Int): K
}