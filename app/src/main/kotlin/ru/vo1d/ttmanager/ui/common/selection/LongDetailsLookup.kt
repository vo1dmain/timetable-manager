package ru.vo1d.ttmanager.ui.common.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ru.vo1d.ttmanager.ui.utils.extensions.cast

internal class LongDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        
        return if (holder is SelectableViewHolder<*>) holder.getItemDetails().cast()
        else null
    }

}