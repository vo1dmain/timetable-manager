package ru.vo1dmain.timetables.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1dmain.timetables.data.converters.Converters
import ru.vo1dmain.timetables.data.entities.session.Session
import ru.vo1dmain.timetables.data.entities.session.SessionsDao
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacher
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacherDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectsDao
import ru.vo1dmain.timetables.data.entities.teacher.Teacher
import ru.vo1dmain.timetables.data.entities.teacher.TeacherFts
import ru.vo1dmain.timetables.data.entities.teacher.TeachersDao
import ru.vo1dmain.timetables.data.entities.week.Week
import ru.vo1dmain.timetables.data.entities.week.WeeksDao

@Database(
    entities = [
        Subject::class,
        Session::class,
        Teacher::class,
        TeacherFts::class,
        SubjectTeacher::class,
        Week::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class TimetableDb : RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
    abstract fun sessionsDao(): SessionsDao
    abstract fun teachersDao(): TeachersDao
    abstract fun subjectTeacherDao(): SubjectTeacherDao
    abstract fun weeksDao(): WeeksDao
    
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