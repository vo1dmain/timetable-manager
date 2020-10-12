package com.vo1d.schedulemanager.v2.data.classes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClassRepository extends BaseRepository<Class, ClassDao> {

    public ClassRepository(Application application) {
        Database database = Database.getInstance(application);
        dao = database.classDao();
    }

    ClassWithSubject findClassById2(int id) {
        FindClassById2AsyncTask task = new FindClassById2AsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    LiveData<List<ClassWithSubject>> findAllClassesForADay2(int dayId) {
        FindAllClassesForADay2AsyncTask task = new FindAllClassesForADay2AsyncTask(dao);
        task.execute(dayId);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    private static final class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ClassDao dao;

        private DeleteAllAsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class FindClassById2AsyncTask extends AsyncTask<Integer, Void, ClassWithSubject> {

        private ClassDao dao;

        private FindClassById2AsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected ClassWithSubject doInBackground(Integer... integers) {
            return dao.findClassById2(integers[0]);
        }
    }

    private static final class FindAllClassesForADay2AsyncTask extends AsyncTask<Integer, Void, LiveData<List<ClassWithSubject>>> {
        private ClassDao dao;

        private FindAllClassesForADay2AsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<ClassWithSubject>> doInBackground(Integer... integers) {
            return dao.findAllClassesForADay2(integers[0]);
        }
    }
}
