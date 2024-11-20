package ru.vo1dmain.ttmanager.ui.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ru.vo1dmain.ttmanager.utils.extensions.cast

class LongDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        
        return if (holder is SelectableViewHolder<*>) holder.getItemDetails().cast()
        else null
    }
    
}