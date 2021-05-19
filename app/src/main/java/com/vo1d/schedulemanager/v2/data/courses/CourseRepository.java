package com.vo1d.schedulemanager.v2.data.courses;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.ScheduleDb;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseRepository extends BaseRepository<Course, CourseDao> {

    public CourseRepository(Application application) {
        super(ScheduleDb.getInstance(application).courseDao());
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    LiveData<List<CourseWithInstructors>> getAll() {
        GetAllAsyncTask task = new GetAllAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<CourseWithInstructors> getFiltered(String filter) {
        GetFilteredAsyncTask task = new GetFilteredAsyncTask(dao);
        task.execute(filter);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    CourseWithInstructors findById(int id) {
        FindByIdAsyncTask task = new FindByIdAsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private final CourseDao dao;

        private DeleteAllAsyncTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class GetAllAsyncTask extends AsyncTask<Void, Void, LiveData<List<CourseWithInstructors>>> {


        private final CourseDao dao;

        private GetAllAsyncTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<CourseWithInstructors>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    private static final class GetFilteredAsyncTask extends AsyncTask<String, Void, List<CourseWithInstructors>> {
        private final CourseDao dao;

        private GetFilteredAsyncTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<CourseWithInstructors> doInBackground(String... strings) {
            return dao.getFiltered(strings[0]);
        }
    }

    private static final class FindByIdAsyncTask extends AsyncTask<Integer, Void, CourseWithInstructors> {

        private final CourseDao dao;

        private FindByIdAsyncTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected CourseWithInstructors doInBackground(Integer... integers) {
            return dao.findById(integers[0]);
        }
    }

}
