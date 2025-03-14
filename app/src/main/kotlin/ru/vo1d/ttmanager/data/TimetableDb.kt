package ru.vo1d.ttmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1d.ttmanager.data.converters.Converters
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.instructors.InstructorFts
import ru.vo1d.ttmanager.data.entities.instructors.InstructorsDao
import ru.vo1d.ttmanager.data.entities.sessions.Session
import ru.vo1d.ttmanager.data.entities.sessions.SessionsDao
import ru.vo1d.ttmanager.data.entities.subjectinstructors.SubjectInstructor
import ru.vo1d.ttmanager.data.entities.subjectinstructors.SubjectInstructorsDao
import ru.vo1d.ttmanager.data.entities.subjects.Subject
import ru.vo1d.ttmanager.data.entities.subjects.SubjectsDao
import ru.vo1d.ttmanager.data.entities.weeks.Week
import ru.vo1d.ttmanager.data.entities.weeks.WeeksDao

@Database(
    entities = [
        Subject::class,
        Session::class,
        Instructor::class,
        InstructorFts::class,
        SubjectInstructor::class,
        Week::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TimetableDb : RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
    abstract fun sessionsDao(): SessionsDao
    abstract fun instructorsDao(): InstructorsDao
    abstract fun subjectInstructorsDao(): SubjectInstructorsDao
    abstract fun weeksDao(): WeeksDao
    
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