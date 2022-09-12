package ru.vo1d.timetablemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1d.timetablemanager.data.converters.Converters
import ru.vo1d.timetablemanager.data.entities.instructors.Instructor
import ru.vo1d.timetablemanager.data.entities.instructors.InstructorsDao
import ru.vo1d.timetablemanager.data.entities.sessions.Session
import ru.vo1d.timetablemanager.data.entities.sessions.SessionsDao
import ru.vo1d.timetablemanager.data.entities.subjectinstructors.SubjectInstructor
import ru.vo1d.timetablemanager.data.entities.subjectinstructors.SubjectInstructorsDao
import ru.vo1d.timetablemanager.data.entities.subjects.Subject
import ru.vo1d.timetablemanager.data.entities.subjects.SubjectsDao
import ru.vo1d.timetablemanager.data.entities.weeks.Week
import ru.vo1d.timetablemanager.data.entities.weeks.WeekDao

@Database(
    entities = [
        Subject::class,
        Session::class,
        Instructor::class,
        SubjectInstructor::class,
        Week::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TimetableDb : RoomDatabase() {
    abstract fun courseDao(): SubjectsDao
    abstract fun classDao(): SessionsDao
    abstract fun instructorDao(): InstructorsDao
    abstract fun subjectInstructorDao(): SubjectInstructorsDao
    abstract fun weekDao(): WeekDao

    companion object {
        private lateinit var db: TimetableDb

        @Synchronized
        fun instance(context: Context): TimetableDb {
            if (Companion::db.isInitialized.not()) db = Room
                .databaseBuilder(context, TimetableDb::class.java, "timetable_db")
                .build()

            return db
        }
    }
}