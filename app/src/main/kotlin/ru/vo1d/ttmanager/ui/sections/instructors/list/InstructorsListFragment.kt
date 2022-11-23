package ru.vo1d.ttmanager.ui.sections.instructors.list

import android.os.Bundle
import android.view.MenuItem
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
import ru.vo1d.ttmanager.MainActivity
import ru.vo1d.ttmanager.R
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.databinding.FragmentInstructorsListBinding
import ru.vo1d.ttmanager.ui.dialogs.ConfirmationDialog

class InstructorsListFragment : Fragment(R.layout.fragment_instructors_list) {
    private var _binding: FragmentInstructorsListBinding? = null
    private val binding get() = _binding!!

    private val addInstructor = InstructorsListFragmentDirections.addInstructor()

    private val adapter by lazy { InstructorsListAdapter() }
    private val viewModel by viewModels<InstructorsViewModel>()

    private lateinit var actionDeleteAll: MenuItem


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
        }

        actionDeleteAll = binding.toolbar.menu.findItem(R.id.action_delete_all)

        binding.list.adapter = adapter
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
            viewModel::deleteAll
        ).show(parentFragmentManager, "confirm_deletion_dialog")
    }
}