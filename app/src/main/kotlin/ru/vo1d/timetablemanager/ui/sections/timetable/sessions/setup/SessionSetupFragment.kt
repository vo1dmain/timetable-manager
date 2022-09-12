package ru.vo1d.timetablemanager.ui.sections.timetable.sessions.setup

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import ru.vo1d.timetablemanager.R
import ru.vo1d.timetablemanager.data.DatabaseEntity
import ru.vo1d.timetablemanager.data.entities.instructors.Instructor
import ru.vo1d.timetablemanager.data.entities.sessions.SessionType
import ru.vo1d.timetablemanager.data.entities.subjects.Subject
import ru.vo1d.timetablemanager.databinding.ChipInstructorActionBinding
import ru.vo1d.timetablemanager.databinding.ChipSessionTypeChoiceBinding
import ru.vo1d.timetablemanager.databinding.FragmentSessionSetupBinding


open class SessionSetupFragment : Fragment(R.layout.fragment_session_setup) {
    private lateinit var actionSubmit: MenuItem

    private var buildingWatcher: TextWatcher? = null
    private var roomWatcher: TextWatcher? = null
    private var startTimeWatcher: TextWatcher? = null
    private var endTimeWatcher: TextWatcher? = null

    private var _binding: FragmentSessionSetupBinding? = null
    protected val binding get() = _binding!!

    protected val subjectsAdapter = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_spinner_item,
        emptyList<Subject>()
    )

    protected open val viewModel by viewModels<SessionSetupViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = SessionSetupFragmentArgs.fromBundle(it)
            viewModel.setWeek(args.weekId)
            viewModel.setDay(args.day)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.place.buildingInput.removeTextChangedListener(buildingWatcher)
        binding.place.roomInput.removeTextChangedListener(roomWatcher)
        binding.time.startTimeInput.removeTextChangedListener(startTimeWatcher)
        binding.time.endTimeInput.removeTextChangedListener(endTimeWatcher)

        buildingWatcher = null
        roomWatcher = null
        startTimeWatcher = null
        endTimeWatcher = null

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSessionSetupBinding.bind(view)

        actionSubmit = binding.toolbar.menu.findItem(R.id.action_submit)
        actionSubmit.setOnMenuItemClickListener {
            viewModel.submit()
            findNavController().navigateUp()
        }

        binding.toolbar.setupWithNavController(findNavController())

        binding.subject.subjectInput.setAdapter(subjectsAdapter)
        binding.subject.subjectInput.onItemSelectedListener = SubjectSelectedListener()

        binding.instructor.list.onItemChipSelected<Instructor> {
            viewModel.setInstructorId(it?.id ?: DatabaseEntity.INVALID_ID)
        }
        binding.type.list.onItemChipSelected<SessionType>(viewModel::setType)

        buildingWatcher = binding.place.buildingInput.afterTextChanged(viewModel::setBuilding)
        roomWatcher = binding.place.roomInput.afterTextChanged(viewModel::setRoom)
        startTimeWatcher = binding.time.startTimeInput.afterTextChanged {
            viewModel.setStartTime(LocalTime.parse(it))
        }
        endTimeWatcher = binding.time.endTimeInput.afterTextChanged {
            viewModel.setEndTime(LocalTime.parse(it))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.canBeSubmitted.collectLatest(actionSubmit::setEnabled)
                }
                launch {
                    viewModel.subjects.collectLatest(::onSubjectsLoaded)
                }
                launch {
                    viewModel.subjectIsSet.collectLatest { onSubjectSet(it, this) }
                }
                launch {
                    viewModel.instructorIsSet.collectLatest { onInstructorSet(it, this) }
                }
                launch {
                    viewModel.typeIsSet.collectLatest { onTypeIsSet(it, this) }
                }
                launch {
                    viewModel.placeIsSet.collectLatest { onPlaceIsSet(it, this) }
                }
            }
        }
    }


    private fun onSubjectSet(isSet: Boolean, scope: CoroutineScope) {
        if (isSet.not()) return

        binding.instructor.root.isVisible = true
        scope.launch { viewModel.instructors.collectLatest(::onInstructorsLoaded) }
    }

    private fun onInstructorSet(isSet: Boolean, scope: CoroutineScope) {
        if (isSet.not()) return

        binding.type.root.isVisible = true
        scope.launch { viewModel.types.collectLatest(::onTypesLoaded) }
    }

    private fun onTypeIsSet(isSet: Boolean, scope: CoroutineScope) {
        if (isSet.not()) return

        binding.place.root.isVisible = true
        scope.launch {
            viewModel.buildingNumber.collectLatest(binding.place.buildingInput::setText)
        }
        scope.launch {
            viewModel.roomNumber.collectLatest(binding.place.roomInput::setText)
        }
    }

    private fun onPlaceIsSet(isSet: Boolean, scope: CoroutineScope) {
        if (isSet.not()) return

        binding.time.root.isVisible = true
        scope.launch {
            viewModel.startTime.collectLatest { binding.time.startTimeInput.setText(it.toString()) }
        }
        scope.launch {
            viewModel.endTime.collectLatest { binding.time.endTimeInput.setText(it.toString()) }
        }
    }


    private fun onSubjectsLoaded(subjects: List<Subject>) {
        subjectsAdapter.clear()
        subjectsAdapter.addAll(subjects)
    }

    private fun onInstructorsLoaded(instructors: List<Instructor>) {
        binding.instructor.list.clearCheck()
        binding.instructor.list.removeAllViewsInLayout()

        instructors.forEach {
            binding.instructor.list.addItemChip(
                it,
                ChipInstructorActionBinding::inflate,
                it::shortName
            )
        }
    }

    private fun onTypesLoaded(types: List<SessionType>) {
        binding.type.list.clearCheck()
        binding.type.list.removeAllViewsInLayout()
        val localizedNames = resources.getStringArray(R.array.session_types)

        types.forEach {
            binding.type.list.addItemChip(
                it,
                ChipSessionTypeChoiceBinding::inflate
            ) { localizedNames[it.ordinal] }
        }
    }


    private inline fun <I, B> ChipGroup.addItemChip(
        item: I,
        inflater: (LayoutInflater, ViewGroup, Boolean) -> B,
        textSupplier: () -> String
    ) where B : ViewBinding {
        val chip = inflater(layoutInflater, this, false).root as Chip
        chip.apply {
            text = textSupplier()
            isClickable = true
            isCheckable = true
            tag = item
        }
        addView(chip)
    }


    private inner class SubjectSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position)
            if (item is Subject) viewModel.setSubjectId(item.id)
        }

        override fun onNothingSelected(parent: AdapterView<*>) = Unit
    }


    companion object {
        private inline fun <reified I> ChipGroup.onItemChipSelected(crossinline callback: (I?) -> Unit) {
            setOnCheckedStateChangeListener { _, checked ->
                val selectedChip = findViewById<Chip>(checked.first())
                val item = selectedChip?.tag as? I
                callback(item)
            }
        }

        private inline fun TextInputEditText.afterTextChanged(crossinline action: (String) -> Unit) =
            doAfterTextChanged { action(it?.toString() ?: return@doAfterTextChanged) }
    }
}