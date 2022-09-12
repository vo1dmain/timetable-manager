package ru.vo1d.timetablemanager.ui.sections.instructors.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vo1d.timetablemanager.MainActivity
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.FragmentInstructorsListBinding

class InstructorsListFragment : Fragment(R.layout.fragment_instructors_list) {
    private var _binding: FragmentInstructorsListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { InstructorsListAdapter() }
    private val viewModel by viewModels<InstructorsViewModel>()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInstructorsListBinding.bind(view)

        binding.toolbar.setupWithNavController(
            findNavController(),
            MainActivity.appBarConfiguration
        )

        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.all.collectLatest(adapter::submitList)
                }
            }
        }
    }
}