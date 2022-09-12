package ru.vo1d.timetablemanager.ui.sections.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)
    }
}