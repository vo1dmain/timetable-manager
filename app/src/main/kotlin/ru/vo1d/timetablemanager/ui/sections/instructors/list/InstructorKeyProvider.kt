package ru.vo1d.timetablemanager.ui.sections.instructors.list

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

internal class InstructorKeyProvider(
    private val adapter: InstructorsListAdapter,
    private val recyclerView: RecyclerView
) : ItemKeyProvider<Long>(SCOPE_MAPPED) {

    override fun getKey(position: Int) =
        adapter.getItemId(position)

    override fun getPosition(key: Long): Int {
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
    }
}