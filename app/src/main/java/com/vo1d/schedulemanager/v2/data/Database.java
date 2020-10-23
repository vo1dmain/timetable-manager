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
import com.vo1d.schedulemanager.v2.data.weeks.Week;
import com.vo1d.schedulemanager.v2.data.weeks.WeekDao;

@androidx.room.Database(entities = {Day.class, Subject.class, Class.class, Lecturer.class, Week.class}, version = 5)
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS new_class_table (" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "subjectId INTEGER NOT NULL REFERENCES subject_table(id) ON DELETE CASCADE," +
                    "dayId INTEGER NOT NULL REFERENCES day_table(id) ON DELETE CASCADE," +
                    "audienceBuilding INTEGER NOT NULL," +
                    "audienceCabinet INTEGER NOT NULL," +
                    "startTimeHour INTEGER NOT NULL," +
                    "startTimeMinutes INTEGER NOT NULL," +
                    "endTimeHour INTEGER NOT NULL," +
                    "endTimeMinutes INTEGER NOT NULL," +
                    "type TEXT)");

            database.execSQL("PRAGMA foreign_keys=off;");
            database.execSQL("INSERT INTO new_class_table" +
                    "(id, subjectId, dayId, audienceBuilding, " +
                    "audienceCabinet, startTimeHour, startTimeMinutes," +
                    "endTimeHour, endTimeMinutes) " +
                    "SELECT id, subjectId, dayId, audienceBuilding, " +
                    "audienceCabinet, startTimeHour, startTimeMinutes," +
                    "endTimeHour, endTimeMinutes " +
                    "FROM class_table;"
            );

            database.execSQL("DROP TABLE class_table");
            database.execSQL("PRAGMA foreign_keys=on");
            database.execSQL("ALTER TABLE new_class_table RENAME TO class_table");

            database.execSQL("CREATE UNIQUE INDEX index_class_table_id ON class_table (id)");
            database.execSQL("CREATE INDEX index_class_table_subject_id ON class_table (subjectId)");
            database.execSQL("CREATE INDEX index_class_table_day_id ON class_table (dayId)");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE lecturer_table ADD COLUMN phoneNumber TEXT");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS week_table (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "availableDays TEXT DEFAULT 'Monday,Tuesday,Wednesday,Thursday,Friday,Saturday')"
            );
            database.execSQL("CREATE UNIQUE INDEX index_week_table_id ON week_table (id)");

            database.execSQL("ALTER TABLE day_table ADD COLUMN weekId INTEGER NOT NULL DEFAULT 1 REFERENCES week_table(id) ON DELETE CASCADE");
            database.execSQL("CREATE INDEX index_day_table_weekId ON day_table (weekId)");

            database.execSQL("INSERT INTO week_table(title, availableDays) VALUES(\"Week 1\", \"Saturday\")");
        }
    };

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract DayDao dayDao();

    public abstract SubjectDao subjectDao();

    public abstract ClassDao classDao();

    public abstract LecturerDao lecturerDao();

    public abstract WeekDao weekDao();

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
            weekDao.insert(new Week[]{w});

            for (int i = 0; i <= 5; i++) {
                Day d = new Day(i, w.id);
                dayDao.insert(new Day[]{d});
            }
            return null;
        }
    }
}
