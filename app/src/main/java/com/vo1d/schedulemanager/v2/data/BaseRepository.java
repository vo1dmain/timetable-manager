package com.vo1d.schedulemanager.v2.data;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;


@SuppressWarnings("unchecked")
public abstract class BaseRepository<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>> {

    protected final DaoType dao;

    protected BaseRepository(DaoType dao) {
        this.dao = dao;
    }

    public void insert(ItemType[] items) {
        new InsertAsyncTask<>(dao).execute(items);
    }

    public long insert(ItemType item) {
        InsertReturnIdAsyncTask<ItemType, DaoType> task = new InsertReturnIdAsyncTask<>(dao);
        task.execute(item);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return -1;
        }
    }

    public void update(ItemType item) {
        new UpdateAsyncTask<>(dao).execute(item);
    }

    public void delete(ItemType[] items) {
        new DeleteAsyncTask<>(dao).execute(items);
    }

    private static class InsertAsyncTask<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>>
            extends AsyncTask<ItemType, Void, Void> {

        private final DaoType dao;

        private InsertAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType[] items) {
            dao.insert(items);
            return null;
        }
    }

    private static class InsertReturnIdAsyncTask<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>>
            extends AsyncTask<ItemType, Void, Long> {

        private final DaoType dao;

        private InsertReturnIdAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(ItemType[] items) {
            return dao.insert(items[0]);
        }
    }

    private static class UpdateAsyncTask<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>>
            extends AsyncTask<ItemType, Void, Void> {

        private final DaoType dao;

        private UpdateAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType[] items) {
            dao.update(items[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>>
            extends AsyncTask<ItemType, Void, Void> {

        private final DaoType dao;

        private DeleteAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType[] items) {
            dao.delete(items);
            return null;
        }
    }
}
