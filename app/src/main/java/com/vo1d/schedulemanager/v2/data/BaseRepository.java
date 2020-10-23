package com.vo1d.schedulemanager.v2.data;

import android.os.AsyncTask;


@SuppressWarnings("unchecked")
public abstract class BaseRepository<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>> {

    protected DaoType dao;

    public void insert(ItemType[] items) {
        new InsertAsyncTask<>(dao).execute(items);
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

    private static class UpdateAsyncTask<ItemType extends IMyEntity, DaoType extends IBaseDao<ItemType>>
            extends AsyncTask<ItemType, Void, Void> {

        private final DaoType dao;

        private UpdateAsyncTask(DaoType dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ItemType[] subjects) {
            dao.update(subjects[0]);
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
        protected Void doInBackground(ItemType[] subjects) {
            dao.delete(subjects);
            return null;
        }
    }
}
