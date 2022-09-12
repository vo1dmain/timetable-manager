package ru.vo1d.timetablemanager.ui.sections.instructors.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.FragmentInstructorSetupBinding

internal class InstructorSetupFragment : Fragment(R.layout.fragment_instructor_setup) {
    private var _binding: FragmentInstructorSetupBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<InstructorSetupViewModel>()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInstructorSetupBinding.bind(view)
    }
}