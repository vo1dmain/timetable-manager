package ru.vo1d.timetablemanager.ui.sections.timetable

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.FragmentTimetableBinding

internal class TimetableFragment : Fragment(R.layout.fragment_timetable) {
    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!

    private val daysAdapter by lazy { DaysAdapter(this) }
    private val viewModel by viewModels<TimetableViewModel>()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTimetableBinding.bind(view)

        binding.daysPager.adapter = daysAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {

                }
            }
        }
    }
}