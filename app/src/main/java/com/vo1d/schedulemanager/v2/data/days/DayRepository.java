package com.vo1d.schedulemanager.v2.data.days;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

class DayRepository extends BaseRepository<Day, DayDao> {

    DayRepository(Application application) {
        super(Database.getInstance(application).dayDao());
    }

    LiveData<List<Day>> getAll() {
        GetAllAsyncTask task = new GetAllAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class GetAllAsyncTask extends AsyncTask<Void, Void, LiveData<List<Day>>> {
        private final DayDao dao;

        public GetAllAsyncTask(DayDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Day>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }
}
