package ru.vo1d.timetablemanager.ui.sections.subjects.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.FragmentSubjectSetupBinding

internal class SubjectSetupFragment : Fragment(R.layout.fragment_subject_setup) {
    private var _binding: FragmentSubjectSetupBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SubjectSetupViewModel>()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSubjectSetupBinding.bind(view)
    }
}