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
        version = 9)
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

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS new_class_table (" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "subjectId INTEGER NOT NULL REFERENCES subject_table(id) ON DELETE CASCADE," +
                    "dayId INTEGER NOT NULL REFERENCES day_table(id) ON DELETE CASCADE," +
                    "audienceBuilding INTEGER NOT NULL," +
                    "audienceCabinet INTEGER NOT NULL," +
                    "startTime TEXT DEFAULT '00:00'," +
                    "endTime TEXT DEFAULT '00:00'," +
                    "type TEXT)");

            database.execSQL("PRAGMA foreign_keys=off;");
            database.execSQL("INSERT INTO new_class_table" +
                    "(id, subjectId, dayId, audienceBuilding, " +
                    "audienceCabinet, type) " +
                    "SELECT id, subjectId, dayId, audienceBuilding, " +
                    "audienceCabinet, type " +
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

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE subject_table RENAME TO course_table");
            database.execSQL("ALTER TABLE lecturer_table RENAME TO instructor_table");

            database.execSQL("CREATE TABLE IF NOT EXISTS new_class_table (" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "courseId INTEGER NOT NULL REFERENCES course_table(id) ON DELETE CASCADE," +
                    "dayId INTEGER NOT NULL REFERENCES day_table(id) ON DELETE CASCADE," +
                    "audienceBuilding INTEGER NOT NULL," +
                    "audienceCabinet INTEGER NOT NULL," +
                    "startTime TEXT DEFAULT '00:00'," +
                    "endTime TEXT DEFAULT '00:00'," +
                    "type TEXT)");

            database.execSQL("PRAGMA foreign_keys=off;");
            database.execSQL("INSERT INTO new_class_table" +
                    "(id, courseId, dayId, audienceBuilding, " +
                    "audienceCabinet, startTime, endTime, type) " +
                    "SELECT id, subjectId, dayId, audienceBuilding, " +
                    "audienceCabinet, startTime, endTime, type " +
                    "FROM class_table;"
            );

            database.execSQL("DROP TABLE class_table");
            database.execSQL("PRAGMA foreign_keys=on");
            database.execSQL("ALTER TABLE new_class_table RENAME TO class_table");

            database.execSQL("CREATE UNIQUE INDEX index_class_table_id ON class_table (id)");
            database.execSQL("CREATE INDEX index_class_table_course_id ON class_table (courseId)");
            database.execSQL("CREATE INDEX index_class_table_day_id ON class_table (dayId)");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS course_instructor_table (" +
                    "courseId INTEGER NOT NULL, " +
                    "instructorId INTEGER NOT NULL, " +
                    "PRIMARY KEY (courseId, instructorId))");

            database.execSQL("CREATE INDEX IF NOT EXISTS `index_course_instructor_table_instructorId`" +
                    "on `course_instructor_table` (`instructorId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_course_instructor_table_courseId`" +
                    "on `course_instructor_table` (`courseId`)");

            database.execSQL("CREATE TABLE IF NOT EXISTS `new_course_table` (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title TEXT," +
                    "courseTypes TEXT)");

            database.execSQL("PRAGMA foreign_keys=off;");
            database.execSQL("INSERT INTO new_course_table" +
                    "(id, title, courseTypes) " +
                    "SELECT `id`, `title`, `subjectTypes`" +
                    "FROM course_table;"
            );

            database.execSQL("DROP TABLE course_table");
            database.execSQL("PRAGMA foreign_keys=on");
            database.execSQL("ALTER TABLE new_course_table RENAME TO course_table");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_course_table_id` ON `course_table` (`id`)");

            database.execSQL("ALTER TABLE class_table ADD COLUMN `instructorId` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_class_table_instructorId` on `class_table` (`instructorId`)");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS new_course_instructor_table (" +
                    "courseId INTEGER NOT NULL REFERENCES course_table(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "instructorId INTEGER NOT NULL REFERENCES instructor_table(id) ON DELETE CASCADE, " +
                    "PRIMARY KEY (courseId, instructorId))");

            database.execSQL("PRAGMA foreign_keys=off;");
            database.execSQL("INSERT INTO new_course_instructor_table" +
                    "(courseId, instructorId) " +
                    "SELECT `courseId`, `instructorId`" +
                    "FROM course_instructor_table;"
            );

            database.execSQL("DROP TABLE course_instructor_table");
            database.execSQL("PRAGMA foreign_keys=on");
            database.execSQL("ALTER TABLE new_course_instructor_table RENAME TO course_instructor_table");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_course_instructor_table_instructorId`" +
                    "on `course_instructor_table` (`instructorId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_course_instructor_table_courseId`" +
                    "on `course_instructor_table` (`courseId`)");
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
                    .addMigrations(MIGRATION_1_2,
                            MIGRATION_2_3,
                            MIGRATION_3_4,
                            MIGRATION_4_5,
                            MIGRATION_5_6,
                            MIGRATION_6_7,
                            MIGRATION_7_8,
                            MIGRATION_8_9)
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
            weekDao.insert(new Week[]{w});

            for (int i = 0; i <= 5; i++) {
                Day d = new Day(i, w.id);
                dayDao.insert(new Day[]{d});
            }
            return null;
        }
    }
}
