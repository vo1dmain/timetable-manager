package ru.vo1dmain.timetables.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vo1dmain.timetables.data.converters.Converters
import ru.vo1dmain.timetables.data.entities.event.EventDao
import ru.vo1dmain.timetables.data.entities.event.EventEntity
import ru.vo1dmain.timetables.data.entities.subject.SubjectDao
import ru.vo1dmain.timetables.data.entities.subject.SubjectEntity
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacher
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacherDao
import ru.vo1dmain.timetables.data.entities.teacher.TeacherDao
import ru.vo1dmain.timetables.data.entities.teacher.TeacherEntity

@Database(
    entities = [
        SubjectEntity::class,
        EventEntity::class,
        TeacherEntity::class,
        SubjectTeacher::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
internal abstract class TimetableDb : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun eventDao(): EventDao
    abstract fun teacherDao(): TeacherDao
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