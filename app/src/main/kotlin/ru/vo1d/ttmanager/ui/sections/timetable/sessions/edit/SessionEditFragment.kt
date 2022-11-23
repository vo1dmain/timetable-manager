package ru.vo1d.ttmanager.ui.sections.timetable.sessions.edit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vo1d.ttmanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.sessions.SessionType
import ru.vo1d.ttmanager.ui.sections.timetable.sessions.setup.SessionSetupFragment

class SessionEditFragment : SessionSetupFragment() {
    override val viewModel by viewModels<SessionEditViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = SessionEditFragmentArgs.fromBundle(it)
            viewModel.setItemId(args.sessionId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.subjectId.collectLatest(::onSubjectIdSet) }
                launch { viewModel.instructorId.collectLatest(::onInstructorIdSet) }
                launch { viewModel.selectedType.collectLatest(::onTypeSelected) }
            }
        }
    }


    private fun onSubjectIdSet(id: Int) {
        val index = subjectsAdapter.findIndex { it.id == id }
        binding.subject.subjectInput.setSelection(index)
    }

    private fun onInstructorIdSet(id: Int) {
        if (id == INVALID_ID) return binding.instructor.list.clearCheck()

        val chip = binding.instructor.list.findViewByPredicate<Chip> {
            val tag = it.tag
            tag is Instructor && tag.id == id
        } ?: return

        binding.instructor.list.check(chip.id)
    }

    private fun onTypeSelected(type: SessionType?) {
        if (type == null) return binding.type.list.clearCheck()

        val chip = binding.type.list.findViewByPredicate<Chip> {
            val tag = it.tag
            tag is SessionType && tag == type
        } ?: return

        binding.type.list.check(chip.id)
    }


    companion object {
        private inline fun <reified T : View> ChipGroup.findViewByPredicate(predicate: (T) -> Boolean): T? {
            forEach {
                val view = it as? T ?: return null
                if (predicate(view)) return view
            }
            return null
        }

        internal inline fun <T> ArrayAdapter<T>.findIndex(predicate: (T) -> Boolean): Int {
            for (index in 0 until count) {
                val item = getItem(index) ?: return -1
                if (predicate(item)) return index
            }
            return -1
        }
    }
}