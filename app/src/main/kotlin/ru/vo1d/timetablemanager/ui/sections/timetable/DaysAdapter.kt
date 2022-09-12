package ru.vo1d.timetablemanager.ui.sections.timetable

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import ru.vo1d.timetablemanager.ui.sections.timetable.sessions.list.SessionsListFragment

internal class DaysAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var days = emptyList<DayOfWeek>()


    override fun containsItem(itemId: Long) =
        days.contains(DayOfWeek.of(itemId.toInt()))

    override fun getItemId(position: Int) =
        days[position].isoDayNumber.toLong()

    override fun createFragment(position: Int) =
        SessionsListFragment(days[position])

    override fun getItemCount() =
        days.size


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<DayOfWeek>) {
        this.days = list
        notifyDataSetChanged()
    }
}