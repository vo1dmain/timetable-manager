package ru.vo1dmain.timetables.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1dmain.timetables.data.converters.Converters
import ru.vo1dmain.timetables.data.entities.event.Event
import ru.vo1dmain.timetables.data.entities.event.EventsDao
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacher
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacherDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectsDao
import ru.vo1dmain.timetables.data.entities.teacher.Teacher
import ru.vo1dmain.timetables.data.entities.teacher.TeachersDao

@Database(
    entities = [
        Subject::class,
        Event::class,
        Teacher::class,
        SubjectTeacher::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class TimetableDb : RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
    abstract fun eventsDao(): EventsDao
    abstract fun teachersDao(): TeachersDao
    abstract fun subjectTeacherDao(): SubjectTeacherDao
    
    companion object {
        private var db: TimetableDb? = null
        
        @Synchronized
        fun instance(context: Context): TimetableDb {
            if (db == null)
                db = Room.databaseBuilder(context, TimetableDb::class.java, "timetable_db")
                    .fallbackToDestructiveMigration()
                    .build()
            
            return db as TimetableDb
        }
    }
}