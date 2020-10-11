package com.vo1d.schedulemanager.v2.data.subject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

class SubjectsRepository extends BaseRepository<Subject, SubjectsDao> {

    SubjectsRepository(Application application) {
        Database database = Database.getInstance(application);
        dao = database.subjectsDao();
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    LiveData<List<Subject>> getAllSubjects() {
        GetAllSubjectsAsyncTask task = new GetAllSubjectsAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<Subject> getFilteredSubjects(String filter) {
        GetFilteredSubjectsAsyncTask task = new GetFilteredSubjectsAsyncTask(dao);
        task.execute(filter);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    Subject findSubjectById(int id) {
        FindSubjectByIdAsyncTask task = new FindSubjectByIdAsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private SubjectsDao subjectsDao;

        private DeleteAllAsyncTask(SubjectsDao subjectsDao) {
            this.subjectsDao = subjectsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subjectsDao.deleteAll();
            return null;
        }
    }

    private static final class GetAllSubjectsAsyncTask extends AsyncTask<Void, Void, LiveData<List<Subject>>> {


        private final SubjectsDao dao;

        private GetAllSubjectsAsyncTask(SubjectsDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Subject>> doInBackground(Void... voids) {
            return dao.getAllSubjects();
        }
    }

    private static final class GetFilteredSubjectsAsyncTask extends AsyncTask<String, Void, List<Subject>> {
        private SubjectsDao subjectsDao;

        private GetFilteredSubjectsAsyncTask(SubjectsDao subjectsDao) {
            this.subjectsDao = subjectsDao;
        }

        @Override
        protected List<Subject> doInBackground(String... strings) {
            return subjectsDao.getFilteredSubjects(strings[0]);
        }
    }

    private static final class FindSubjectByIdAsyncTask extends AsyncTask<Integer, Void, Subject> {

        private SubjectsDao subjectsDao;

        private FindSubjectByIdAsyncTask(SubjectsDao subjectsDao) {
            this.subjectsDao = subjectsDao;
        }

        @Override
        protected Subject doInBackground(Integer... integers) {
            return subjectsDao.findSubjectById(integers[0]);
        }
    }

}
