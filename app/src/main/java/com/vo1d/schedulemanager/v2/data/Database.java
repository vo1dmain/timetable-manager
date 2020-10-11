package com.vo1d.schedulemanager.v2.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassesDao;
import com.vo1d.schedulemanager.v2.data.day.Day;
import com.vo1d.schedulemanager.v2.data.day.DaysDao;
import com.vo1d.schedulemanager.v2.data.subject.Subject;
import com.vo1d.schedulemanager.v2.data.subject.SubjectsDao;

@androidx.room.Database(entities = {Day.class, Subject.class, Class.class}, version = 1)
public abstract class Database extends RoomDatabase {

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
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract DaysDao daysDao();

    public abstract SubjectsDao subjectsDao();

    public abstract ClassesDao classesDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DaysDao daysDao;

        private PopulateDbAsyncTask(Database db) {
            daysDao = db.daysDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 5; i++) {
                Day d = new Day(i);
                daysDao.insert(d);
            }
            return null;
        }
    }
}
