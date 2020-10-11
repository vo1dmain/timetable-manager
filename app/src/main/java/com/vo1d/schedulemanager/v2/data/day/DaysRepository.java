package com.vo1d.schedulemanager.v2.data.day;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

class DaysRepository extends BaseRepository<Day, DaysDao> {

    DaysRepository(Application application) {
        Database database = Database.getInstance(application);
        dao = database.daysDao();
    }

    LiveData<List<Day>> getAllDays() {
        GetAllDaysAsyncTask task = new GetAllDaysAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class GetAllDaysAsyncTask extends AsyncTask<Void, Void, LiveData<List<Day>>> {
        private DaysDao dao;

        public GetAllDaysAsyncTask(DaysDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Day>> doInBackground(Void... voids) {
            return dao.getAllDays();
        }
    }
}
