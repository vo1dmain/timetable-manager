package ru.vo1dmain.ttmanager.sections.subjects.list

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

internal class SubjectDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        
        return if (holder is SubjectsListAdapter.ViewHolder) object : ItemDetails<Long>() {
            override fun getPosition() = holder.getAdapterPosition()
            override fun getSelectionKey() = holder.getItemId()
        } else null
    }
}