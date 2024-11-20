package ru.vo1dmain.timetables.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1dmain.timetables.data.converters.Converters
import ru.vo1dmain.timetables.data.entities.instructor.Instructor
import ru.vo1dmain.timetables.data.entities.instructor.InstructorFts
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsDao
import ru.vo1dmain.timetables.data.entities.session.Session
import ru.vo1dmain.timetables.data.entities.session.SessionsDao
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.subject.SubjectInstructor
import ru.vo1dmain.timetables.data.entities.subject.SubjectInstructorsDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectsDao
import ru.vo1dmain.timetables.data.entities.week.Week
import ru.vo1dmain.timetables.data.entities.week.WeeksDao

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