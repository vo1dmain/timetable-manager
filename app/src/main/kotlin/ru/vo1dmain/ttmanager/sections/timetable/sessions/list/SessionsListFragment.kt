package ru.vo1dmain.ttmanager.sections.timetable.sessions.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.datetime.DayOfWeek
import ru.vo1dmain.ttmanager.R
import ru.vo1dmain.ttmanager.databinding.FragmentSessionsListBinding

class SessionsListFragment() : Fragment(R.layout.fragment_sessions_list) {
    private var _binding: FragmentSessionsListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel by viewModels<SessionsViewModel>()
    
    
    private lateinit var day: DayOfWeek
    
    
    constructor(weekId: Int, dayOfWeek: DayOfWeek) : this() {
        day = dayOfWeek
    }
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (::day.isInitialized) viewModel.setDay(day)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSessionsListBinding.bind(view)
    }
}