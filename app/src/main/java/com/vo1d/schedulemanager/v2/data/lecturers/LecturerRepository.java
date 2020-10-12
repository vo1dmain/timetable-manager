package com.vo1d.schedulemanager.v2.data.lecturers;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

class LecturerRepository extends BaseRepository<Lecturer, LecturerDao> {

    LecturerRepository(Application application) {
        dao = Database.getInstance(application).lecturerDao();
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    Lecturer findLecturerById(int id) {
        FindLecturerByIdAsyncTask task = new FindLecturerByIdAsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<Lecturer> getFilteredLecturers(String filter) {
        GetFilteredLecturersAsyncTask task = new GetFilteredLecturersAsyncTask(dao);
        task.execute(filter);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    LiveData<List<Lecturer>> getAll() {
        GetAllAsyncTask task = new GetAllAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private LecturerDao dao;

        public DeleteAllAsyncTask(LecturerDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class FindLecturerByIdAsyncTask extends AsyncTask<Integer, Void, Lecturer> {
        private LecturerDao dao;

        public FindLecturerByIdAsyncTask(LecturerDao dao) {
            this.dao = dao;
        }

        @Override
        protected Lecturer doInBackground(Integer... integers) {
            return dao.findLecturerById(integers[0]);
        }
    }

    private static final class GetFilteredLecturersAsyncTask extends AsyncTask<String, Void, List<Lecturer>> {
        private LecturerDao dao;

        private GetFilteredLecturersAsyncTask(LecturerDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<Lecturer> doInBackground(String... strings) {
            return dao.getFilteredLecturers(strings[0]);
        }
    }

    private static final class GetAllAsyncTask extends AsyncTask<Void, Void, LiveData<List<Lecturer>>> {
        private LecturerDao dao;

        public GetAllAsyncTask(LecturerDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Lecturer>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }
}
