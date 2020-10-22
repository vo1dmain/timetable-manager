package com.vo1d.schedulemanager.v2.data.subjects;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SubjectRepository extends BaseRepository<Subject, SubjectDao> {

    public SubjectRepository(Application application) {
        Database database = Database.getInstance(application);
        dao = database.subjectDao();
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

        private final SubjectDao dao;

        private DeleteAllAsyncTask(SubjectDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class GetAllSubjectsAsyncTask extends AsyncTask<Void, Void, LiveData<List<Subject>>> {


        private final SubjectDao dao;

        private GetAllSubjectsAsyncTask(SubjectDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Subject>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    private static final class GetFilteredSubjectsAsyncTask extends AsyncTask<String, Void, List<Subject>> {
        private final SubjectDao dao;

        private GetFilteredSubjectsAsyncTask(SubjectDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<Subject> doInBackground(String... strings) {
            return dao.getFilteredSubjects(strings[0]);
        }
    }

    private static final class FindSubjectByIdAsyncTask extends AsyncTask<Integer, Void, Subject> {

        private final SubjectDao dao;

        private FindSubjectByIdAsyncTask(SubjectDao dao) {
            this.dao = dao;
        }

        @Override
        protected Subject doInBackground(Integer... integers) {
            return dao.findSubjectById(integers[0]);
        }
    }

}
