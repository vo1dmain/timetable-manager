package ru.vo1d.timetablemanager.ui.sections.instructors.list

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

internal class InstructorDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)

        return if (holder is InstructorsListAdapter.ViewHolder) object : ItemDetails<Long>() {
            override fun getPosition() = holder.getAdapterPosition()
            override fun getSelectionKey() = holder.getItemId()
        }
        else null
    }

}