package com.vo1d.schedulemanager.v2.data;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

@SuppressWarnings({"unchecked"})
public abstract class BaseRepository<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>> {

    protected DaoType dao;

    public long insert(ItemType item) {
        try {
            InsertAsyncTask task = new InsertAsyncTask(dao);
            task.execute(item);
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void update(ItemType item) {
        new UpdateAsyncTask(dao).execute(item);
    }

    public void delete(ItemType... items) {
        new DeleteAsyncTask(dao).execute(items);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertAsyncTask extends AsyncTask<ItemType, Void, Long> {

        private DaoType dao;

        private InsertAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(ItemType... items) {
            return dao.insert(items[0]);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateAsyncTask extends AsyncTask<ItemType, Void, Void> {

        private DaoType dao;

        private UpdateAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType... subjects) {
            dao.update(subjects[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteAsyncTask extends AsyncTask<ItemType, Void, Void> {

        private DaoType dao;

        private DeleteAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType... subjects) {
            dao.delete(subjects);
            return null;
        }
    }
}
