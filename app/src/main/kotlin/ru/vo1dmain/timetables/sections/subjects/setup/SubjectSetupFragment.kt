package ru.vo1dmain.timetables.sections.subjects.setup

import android.os.Bundle
import android.text.TextWatcher
import android.view.MenuItem
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
import ru.vo1dmain.timetables.R
import ru.vo1dmain.timetables.databinding.FragmentSubjectSetupBinding
import ru.vo1dmain.timetables.ui.extensions.doAfterTextChanged

internal class SubjectSetupFragment : Fragment(R.layout.fragment_subject_setup) {
    private var titleWatcher: TextWatcher? = null
    
    private var _binding: FragmentSubjectSetupBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel by viewModels<SubjectSetupViewModel>()
    
    private lateinit var actionSubmit: MenuItem
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        binding.subjectTitle.removeTextChangedListener(titleWatcher)
        
        titleWatcher = null
        
        _binding = null
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSubjectSetupBinding.bind(view)
        
        binding.toolbar.setupWithNavController(findNavController())
        
        actionSubmit = binding.toolbar.menu.findItem(R.id.action_submit)
        actionSubmit.setOnMenuItemClickListener {
            viewModel.submit {
                //TODO: добавить уведомление
            }
            findNavController().navigateUp()
        }
        
        
        titleWatcher = binding.subjectTitle.doAfterTextChanged(viewModel::setTitle)
        
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.canBeSubmitted.collectLatest(actionSubmit::setEnabled) }
            }
        }
    }
}