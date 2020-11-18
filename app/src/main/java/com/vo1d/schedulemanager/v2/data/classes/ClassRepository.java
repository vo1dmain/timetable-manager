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
        super(Database.getInstance(application).classDao());
    }

    ClassWithCourse findClassById2(int id) {
        FindClassById2AsyncTask task = new FindClassById2AsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    LiveData<List<ClassWithCourse>> findAllClassesForADay2(int dayId) {
        FindAllClassesForADay2AsyncTask task = new FindAllClassesForADay2AsyncTask(dao);
        task.execute(dayId);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    Class[] findAllClassesForADayAsArray(int dayId) {
        FindAllClassesForADayAsArrayAsyncTask task = new FindAllClassesForADayAsArrayAsyncTask(dao);
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

        private final ClassDao dao;

        private DeleteAllAsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class FindClassById2AsyncTask extends AsyncTask<Integer, Void, ClassWithCourse> {

        private final ClassDao dao;

        private FindClassById2AsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected ClassWithCourse doInBackground(Integer... integers) {
            return dao.findClassById2(integers[0]);
        }
    }

    private static final class FindAllClassesForADay2AsyncTask extends AsyncTask<Integer, Void, LiveData<List<ClassWithCourse>>> {
        private final ClassDao dao;

        private FindAllClassesForADay2AsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<ClassWithCourse>> doInBackground(Integer... integers) {
            return dao.findAllClassesForADay2(integers[0]);
        }
    }

    private static final class FindAllClassesForADayAsArrayAsyncTask extends AsyncTask<Integer, Void, Class[]> {
        private final ClassDao dao;

        private FindAllClassesForADayAsArrayAsyncTask(ClassDao dao) {
            this.dao = dao;
        }

        @Override
        protected Class[] doInBackground(Integer... integers) {
            return dao.findAllClassesForADayAsArray(integers[0]);
        }
    }
}
