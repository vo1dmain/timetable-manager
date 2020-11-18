package com.vo1d.schedulemanager.v2.data.courseinstructor;

import android.app.Application;
import android.os.AsyncTask;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

public class CourseInstructorRepository extends BaseRepository<CourseInstructor, CourseInstructorDao> {

    public CourseInstructorRepository(Application application) {
        super(Database.getInstance(application).courseInstructorDao());
    }

    public void update(CourseInstructor... junctions) {
        new UpdateManyAsyncTask(dao).execute(junctions);
    }

    private static class UpdateManyAsyncTask extends AsyncTask<CourseInstructor, Void, Void> {

        private final CourseInstructorDao dao;

        public UpdateManyAsyncTask(CourseInstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(CourseInstructor... junctions) {
            dao.update(junctions);
            return null;
        }
    }
}
