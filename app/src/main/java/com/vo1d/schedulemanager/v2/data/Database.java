package com.vo1d.schedulemanager.v2.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassDao;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructorDao;
import com.vo1d.schedulemanager.v2.data.courses.Course;
import com.vo1d.schedulemanager.v2.data.courses.CourseDao;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.days.DayDao;
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorDao;
import com.vo1d.schedulemanager.v2.data.weeks.Week;
import com.vo1d.schedulemanager.v2.data.weeks.WeekDao;

@androidx.room.Database(
        entities = {Day.class,
                Course.class,
                Class.class,
                Instructor.class,
                Week.class,
                CourseInstructor.class
        },
        version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;
    private static final Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "days_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract DayDao dayDao();

    public abstract CourseDao courseDao();

    public abstract ClassDao classDao();

    public abstract InstructorDao instructorDao();

    public abstract WeekDao weekDao();

    public abstract CourseInstructorDao courseInstructorDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final DayDao dayDao;
        private final WeekDao weekDao;

        private PopulateDbAsyncTask(Database db) {
            dayDao = db.dayDao();
            weekDao = db.weekDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Week w = new Week("Week 1");
            int id = (int) weekDao.insert(w);

            for (int i = 0; i < 6; i++) {
                Day d = new Day(i, id);
                dayDao.insert(new Day[]{d});
            }
            return null;
        }
    }
}
