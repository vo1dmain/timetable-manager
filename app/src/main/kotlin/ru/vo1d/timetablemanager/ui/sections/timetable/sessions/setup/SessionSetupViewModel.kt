package ru.vo1d.timetablemanager.ui.sections.timetable.sessions.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.DEFAULT_ID
import ru.vo1d.timetablemanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1d.timetablemanager.data.entities.instructors.InstructorsRepository
import ru.vo1d.timetablemanager.data.entities.sessions.Session
import ru.vo1d.timetablemanager.data.entities.sessions.SessionType
import ru.vo1d.timetablemanager.data.entities.sessions.SessionsRepository
import ru.vo1d.timetablemanager.data.entities.subjects.SubjectsRepository
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
open class SessionSetupViewModel(application: Application) :
    AndroidViewModel(application) {
    protected val sessionsRepo = SessionsRepository(application)
    private val subjectsRepo = SubjectsRepository(application)
    private val instructorsRepo = InstructorsRepository(application)


    @Suppress("PropertyName")
    protected val _subjectId = MutableStateFlow(INVALID_ID)

    @Suppress("PropertyName")
    protected val _instructorId = MutableStateFlow(INVALID_ID)

    @Suppress("PropertyName")
    protected val _selectedType = MutableStateFlow(SessionType.None)

    private val _buildingNumber = MutableStateFlow("")
    private val _roomNumber = MutableStateFlow("")
    private val _startTime = MutableStateFlow(nowTime().toLocalTime())
    private val _endTime = MutableStateFlow((nowTime() + 45.minutes).toLocalTime())


    private lateinit var day: DayOfWeek
    private var weekId by Delegates.notNull<Int>()


    val subjectIsSet by lazy { _subjectId.mapLatest { it > INVALID_ID } }
    val instructorIsSet by lazy { _instructorId.mapLatest { it > INVALID_ID } }
    val typeIsSet by lazy { _selectedType.mapLatest { true } }
    val placeIsSet by lazy {
        combine(_buildingNumber, _roomNumber) { states ->
            states.all { it.isNotBlank() && it.isNotEmpty() }
        }
    }

    val canBeSubmitted by lazy {
        combine(
            subjectIsSet,
            instructorIsSet,
            placeIsSet,
            typeIsSet
        ) { states -> states.all { it } }
    }

    val subjects by lazy { subjectsRepo.all }
    val instructors by lazy { _subjectId.flatMapLatest { instructorsRepo.findForSubject(it) } }
    val types by lazy { _subjectId.flatMapLatest { subjectsRepo.findTypesFor(it) } }


    val buildingNumber by lazy { _buildingNumber.asStateFlow() }
    val roomNumber by lazy { _roomNumber.asStateFlow() }
    val startTime by lazy { _startTime.asStateFlow() }
    val endTime by lazy { _endTime.asStateFlow() }


    open fun submit() {
        viewModelScope.launch {
            val item = buildItem()
            sessionsRepo.insert(item)
        }
    }

    fun setWeek(weekId: Int) {
        this.weekId = weekId
    }

    fun setDay(day: DayOfWeek) {
        this.day = day
    }

    fun setBuilding(building: String) {
        _buildingNumber.update { building }
    }

    fun setRoom(cabinet: String) {
        _roomNumber.update { cabinet }
    }

    fun setSubjectId(id: Int) {
        _subjectId.update { id }
    }

    fun setInstructorId(id: Int) {
        _instructorId.update { id }
    }

    fun setType(type: SessionType) {
        _selectedType.update { type }
    }

    fun setStartTime(time: LocalTime) {
        _startTime.update { time }
    }

    fun setEndTime(time: LocalTime) {
        _endTime.update { time }
    }


    protected fun buildItem(id: Int = DEFAULT_ID): Session {
        val place = "${_buildingNumber.value}-${_roomNumber.value}"
        return Session(
            id = id,
            subjectId = _subjectId.value,
            instructorId = _instructorId.value,
            weekId = weekId,
            day = day,
            place = place,
            type = _selectedType.value,
            startTime = _startTime.value,
            endTime = _endTime.value
        )
    }


    companion object {
        internal fun nowTime() = Clock.System.now()
        internal fun Instant.toLocalTime() = toLocalDateTime(TimeZone.currentSystemDefault()).time
    }
}