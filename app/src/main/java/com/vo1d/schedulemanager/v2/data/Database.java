package com.vo1d.schedulemanager.v2.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassDao;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.days.DayDao;
import com.vo1d.schedulemanager.v2.data.lecturers.Lecturer;
import com.vo1d.schedulemanager.v2.data.lecturers.LecturerDao;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectDao;

@androidx.room.Database(entities = {Day.class, Subject.class, Class.class, Lecturer.class}, version = 2)
public abstract class Database extends RoomDatabase {

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS lecturer_table (" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "firstName TEXT," +
                    "middleName TEXT," +
                    "lastName TEXT)");

            database.execSQL("CREATE UNIQUE INDEX index_lecturer_table_id ON lecturer_table (id)");
        }
    };

    private static Database instance;
    private static Callback roomCallback = new Callback() {
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
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract DayDao dayDao();

    public abstract SubjectDao subjectDao();

    public abstract ClassDao classDao();

    public abstract LecturerDao lecturerDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DayDao dayDao;

        private PopulateDbAsyncTask(Database db) {
            dayDao = db.dayDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 5; i++) {
                Day d = new Day(i);
                dayDao.insert(d);
            }
            return null;
        }
    }
}
