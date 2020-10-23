package com.vo1d.schedulemanager.v2.data.weeks;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

import java.util.List;
import java.util.concurrent.ExecutionException;

class WeekRepository extends BaseRepository<Week, WeekDao> {
    WeekRepository(Application application) {
        Database db = Database.getInstance(application);
        dao = db.weekDao();
    }

    LiveData<List<WeekWithDays>> getAllWeeks() {
        GetAllWeeksAsyncTask task = new GetAllWeeksAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    LiveData<WeekWithDays> findWeekById(int id) {
        FindWeekByIdAsyncTask task = new FindWeekByIdAsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class GetAllWeeksAsyncTask extends AsyncTask<Void, Void, LiveData<List<WeekWithDays>>> {

        private final WeekDao dao;

        private GetAllWeeksAsyncTask(WeekDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<WeekWithDays>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    private static final class FindWeekByIdAsyncTask extends AsyncTask<Integer, Void, LiveData<WeekWithDays>> {
        private final WeekDao dao;

        private FindWeekByIdAsyncTask(WeekDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<WeekWithDays> doInBackground(Integer... ints) {
            return dao.findWeekById(ints[0]);
        }
    }
}
