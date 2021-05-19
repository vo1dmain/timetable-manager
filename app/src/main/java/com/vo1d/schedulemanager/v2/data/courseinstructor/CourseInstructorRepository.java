package com.vo1d.schedulemanager.v2.data.courseinstructor;

import android.app.Application;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.ScheduleDb;

public class CourseInstructorRepository extends BaseRepository<CourseInstructor, CourseInstructorDao> {

    public CourseInstructorRepository(Application application) {
        super(ScheduleDb.getInstance(application).courseInstructorDao());
    }

// --Commented out by Inspection START (10.02.2021 15:22):
//    public void update(CourseInstructor... junctions) {
//        new UpdateManyAsyncTask(dao).execute(junctions);
//    }
// --Commented out by Inspection STOP (10.02.2021 15:22)

// --Commented out by Inspection START (10.02.2021 15:33):
//    private static class UpdateManyAsyncTask extends AsyncTask<CourseInstructor, Void, Void> {
//
//        private final CourseInstructorDao dao;
//
//        public UpdateManyAsyncTask(CourseInstructorDao dao) {
//            this.dao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(CourseInstructor... junctions) {
//            dao.update(junctions);
//            return null;
//        }
//    }
// --Commented out by Inspection STOP (10.02.2021 15:33)
}
