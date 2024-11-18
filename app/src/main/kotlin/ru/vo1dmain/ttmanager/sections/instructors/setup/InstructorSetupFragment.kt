package ru.vo1dmain.ttmanager.sections.instructors.setup

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
import ru.vo1dmain.ttmanager.R
import ru.vo1dmain.ttmanager.databinding.FragmentInstructorSetupBinding
import ru.vo1dmain.ttmanager.ui.extensions.doAfterTextChanged

internal class InstructorSetupFragment : Fragment(R.layout.fragment_instructor_setup) {
    private var firstNameWatcher: TextWatcher? = null
    private var middleNameWatcher: TextWatcher? = null
    private var lastNameWatcher: TextWatcher? = null
    private var emailWatcher: TextWatcher? = null
    
    private var _binding: FragmentInstructorSetupBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel by viewModels<InstructorSetupViewModel>()
    
    private lateinit var actionSubmit: MenuItem
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        binding.firstNameInput.removeTextChangedListener(firstNameWatcher)
        binding.middleNameInput.removeTextChangedListener(middleNameWatcher)
        binding.lastNameInput.removeTextChangedListener(lastNameWatcher)
        binding.emailInput.removeTextChangedListener(emailWatcher)
        
        firstNameWatcher = null
        middleNameWatcher = null
        lastNameWatcher = null
        emailWatcher = null
        
        _binding = null
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInstructorSetupBinding.bind(view)
        
        binding.toolbar.setupWithNavController(findNavController())
        
        actionSubmit = binding.toolbar.menu.findItem(R.id.action_submit)
        actionSubmit.setOnMenuItemClickListener {
            viewModel.submit {
                //TODO: добавить уведомление
            }
            findNavController().navigateUp()
        }
        
        
        firstNameWatcher = binding.firstNameInput.doAfterTextChanged(viewModel::setFirstName)
        middleNameWatcher = binding.middleNameInput.doAfterTextChanged(viewModel::setMiddleName)
        lastNameWatcher = binding.lastNameInput.doAfterTextChanged(viewModel::setLastName)
        emailWatcher = binding.emailInput.doAfterTextChanged(viewModel::setEmail)
        
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.canBeSubmitted.collectLatest(actionSubmit::setEnabled) }
            }
        }
    }
}