package ru.vo1dmain.timetables.sections.instructors.list.selection

import androidx.recyclerview.selection.ItemKeyProvider
import ru.vo1dmain.timetables.sections.instructors.list.InstructorsListAdapter

internal class InstructorKeyProvider(private val adapter: InstructorsListAdapter) :
    ItemKeyProvider<Long>(SCOPE_MAPPED) {
    
    override fun getKey(position: Int) =
        adapter.getItemId(position)
    
    override fun getPosition(key: Long) =
        adapter.currentList.indexOfFirst { it.id.toLong() == key }
}