package ru.vo1d.timetablemanager.ui.sections.subjects.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import ru.vo1d.timetablemanager.data.entities.subjects.Subject
import ru.vo1d.timetablemanager.databinding.FragmentSubjectsListBinding

internal class SubjectsListFragment : Fragment(R.layout.fragment_subjects_list) {
    private var _binding: FragmentSubjectsListBinding? = null
    private val binding get() = _binding!!

    private val addSubject = SubjectsListFragmentDirections.addSubject()

    private val adapter by lazy { SubjectsListAdapter() }
    private val viewModel by viewModels<SubjectsListViewModel>()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSubjectsListBinding.bind(view)

        binding.toolbar.setupWithNavController(
            findNavController(),
            MainActivity.appBarConfiguration
        )

        binding.list.adapter = adapter

        binding.actionAddSubject.setOnClickListener {
            findNavController().navigate(addSubject)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.all.collectLatest(::onSubjectsListLoaded)
                }
            }
        }
    }


    private fun onSubjectsListLoaded(list: List<Subject>) {
        adapter.submitList(list)

        binding.messageIsEmpty.isVisible = list.isEmpty()
        binding.list.isVisible = list.isNotEmpty()
    }
}