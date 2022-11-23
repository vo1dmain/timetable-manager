package ru.vo1d.timetablemanager.ui.sections.timetable

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import ru.vo1d.timetablemanager.MainActivity
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1d.timetablemanager.data.entities.weeks.Week
import ru.vo1d.timetablemanager.databinding.FragmentTimetableBinding

internal class TimetableFragment : Fragment(R.layout.fragment_timetable) {
    private val daysNames by lazy { resources.getStringArray(R.array.days_of_week_short) }

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TimetableViewModel>()

    private var mediator: TabLayoutMediator? = null

    private val daysAdapter by lazy { DaysAdapter(this) }
    private val weeksAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<Week>()
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTimetableBinding.bind(view)

        binding.toolbar.setupWithNavController(
            findNavController(),
            MainActivity.appBarConfiguration
        )


        binding.weeksSpinner.doOnItemSelected { _, _, position, _ ->
            val id = weeksAdapter.getItem(position)?.id ?: INVALID_ID
            viewModel.selectWeek(id)
        }


        binding.weeksSpinner.adapter = weeksAdapter
        binding.daysPager.adapter = daysAdapter

        mediator = TabLayoutMediator(binding.daysTabs, binding.daysPager) { tab, position ->
            val day = daysAdapter.getItemAt(position) ?: DayOfWeek.MONDAY
            tab.text = daysNames[day.ordinal]
        }.apply(TabLayoutMediator::attach)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.allWeeks.collectLatest(::onWeeksListChanged) }
                launch { viewModel.selectedWeek.collectLatest(daysAdapter::setWeekId) }
                launch { viewModel.daysForCurrentWeek.collectLatest(::onDaysListChanged) }
            }
        }
    }


    private fun onWeeksListChanged(list: List<Week>) {
        weeksAdapter.clear()
        weeksAdapter.addAll(list)
    }

    private fun onDaysListChanged(list: List<DayOfWeek>) {
        daysAdapter.submitList(list)

        binding.daysTabs.isVisible = list.isNotEmpty()
        binding.daysPager.isVisible = list.isNotEmpty()
    }


    companion object {
        internal inline fun AdapterView<*>.doOnItemSelected(
            crossinline action: (AdapterView<*>, View, Int, Long) -> Unit
        ) {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) = action(parent, view, position, id)

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }
}