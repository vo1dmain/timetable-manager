package ru.vo1d.ttmanager.ui.sections.instructors.list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vo1d.ttmanager.MainActivity
import ru.vo1d.ttmanager.R
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.databinding.FragmentInstructorsListBinding
import ru.vo1d.ttmanager.ui.common.selection.LongDetailsLookup
import ru.vo1d.ttmanager.ui.dialogs.ConfirmationDialog
import ru.vo1d.ttmanager.ui.sections.instructors.list.selection.InstructorKeyProvider
import ru.vo1d.ttmanager.ui.utils.extensions.activity
import ru.vo1d.ttmanager.ui.utils.extensions.observe
import ru.vo1d.ttmanager.ui.utils.extensions.selectionTracker
import ru.vo1d.ttmanager.ui.utils.selectionModeCallback

class InstructorsListFragment : Fragment(R.layout.fragment_instructors_list) {
    private var _binding: FragmentInstructorsListBinding? = null
    private val binding get() = _binding!!
    
    private val addInstructor = InstructorsListFragmentDirections.addInstructor()
    
    private val activity by activity<MainActivity>()
    private val viewModel by viewModels<InstructorsViewModel>()
    
    private lateinit var actionDeleteAll: MenuItem
    private lateinit var adapter: InstructorsListAdapter
    private lateinit var actionModeCallback: ActionMode.Callback
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInstructorsListBinding.bind(view)
        
        binding.toolbar.apply {
            setupWithNavController(findNavController(), MainActivity.appBarConfiguration)
            setOnMenuItemClickListener(::onMenuItemClicked)
            actionDeleteAll = menu.findItem(R.id.action_delete_all)
        }
        
        adapter = InstructorsListAdapter().apply {
            binding.list.adapter = this
            
            viewModel.tracker = selectionTracker(
                "instructors-list",
                binding.list,
                InstructorKeyProvider(this),
                LongDetailsLookup(binding.list),
                StorageStrategy.createLongStorage()
            ) {
                withSelectionPredicate(SelectionPredicates.createSelectAnything())
            }.apply { observe(::onSelectionChanged) }
            
            tracker = viewModel.tracker
        }
        
        actionModeCallback = selectionModeCallback(
            viewModel.tracker,
            activity,
            { adapter.currentList.map { it.id.toLong() } },
            { _, _ -> binding.actionAddInstructor.hide(); true },
            onDestroy = { binding.actionAddInstructor.show() }
        ) { _, item ->
            when (item.itemId) {
                R.id.action_delete_selected -> deleteSelected()
            }
            true
        }
        
        binding.actionAddInstructor.setOnClickListener {
            findNavController().navigate(addInstructor)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.all.collectLatest(::onInstructorsListLoaded) }
            }
        }
    }
    
    
    private fun onInstructorsListLoaded(list: List<Instructor>) {
        adapter.submitList(list)
        
        val listIsEmpty = list.isEmpty()
        
        binding.messageIsEmpty.isVisible = listIsEmpty
        binding.list.isVisible = listIsEmpty.not()
        actionDeleteAll.isEnabled = listIsEmpty.not()
    }
    
    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> onDeleteAll()
        }
        return true
    }
    
    private fun onDeleteAll() {
        ConfirmationDialog(
            R.string.dialog_title_delete_multiple_items,
            R.string.dialog_message_delete_all,
            ::deleteAll
        ).show(parentFragmentManager, "confirm_deletion_dialog")
    }
    
    private fun onSelectionChanged(tracker: SelectionTracker<Long>) {
        if (tracker.hasSelection().not()) {
            activity.actionMode?.finish()
            return
        }
        
        if (activity.actionMode == null)
            activity.startSupportActionMode(actionModeCallback)
        
        activity.actionMode?.title = tracker.selection.size().toString()
    }
    
    
    private fun deleteAll() {
        viewModel.deleteAll {
            val message = when (it) {
                true -> R.string.snackbar_message_delete_all_success
                false -> R.string.snackbar_message_delete_failure
            }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undone) {}
                .setAnchorView(binding.actionAddInstructor)
                .show()
        }
    }
    
    private fun deleteSelected() {
        viewModel.deleteSelected {
        
        }
    }
}