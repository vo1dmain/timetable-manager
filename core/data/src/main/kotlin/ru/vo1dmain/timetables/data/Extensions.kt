package ru.vo1dmain.timetables.data

import ru.vo1dmain.timetables.data.entities.event.EventEntity
import ru.vo1dmain.timetables.data.entities.subject.SubjectEntity
import ru.vo1dmain.timetables.data.entities.subject.SubjectWithTeachers
import ru.vo1dmain.timetables.data.entities.teacher.TeacherEntity
import ru.vo1dmain.timetables.data.models.Event
import ru.vo1dmain.timetables.data.models.Subject
import ru.vo1dmain.timetables.data.models.Teacher

internal fun EventEntity.toModel() = Event(
    id = id,
    subjectId = subjectId,
    hostId = teacherId,
    place = place,
    type = type,
    date = date,
    startTime = startTime,
    endTime = endTime,
)

internal fun TeacherEntity.toModel() = Teacher(
    id = id,
    name = name,
    title = title,
    email = email,
    image = image
)

internal fun SubjectWithTeachers.toModel() = Subject(
    id = subject.id,
    title = subject.title,
    types = subject.types,
    teachers = teachers.map(TeacherEntity::toModel)
)

internal fun Event.toEntity() = EventEntity(
    id = id,
    subjectId = subjectId,
    teacherId = hostId,
    place = place,
    type = type,
    date = date,
    startTime = startTime,
    endTime = endTime
)

internal fun Teacher.toEntity() = TeacherEntity(
    id = id,
    name = name,
    title = title,
    email = email,
    image = image
)

internal fun Subject.toEntity() = SubjectEntity(
    id = id,
    title = title,
    types = types
)