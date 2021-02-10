package com.vo1d.schedulemanager.v2.data.days;

import android.app.Application;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.Database;

class DayRepository extends BaseRepository<Day, DayDao> {

    DayRepository(Application application) {
        super(Database.getInstance(application).dayDao());
    }

// --Commented out by Inspection START (10.02.2021 15:22):
//    LiveData<List<Day>> getAll() {
//        GetAllAsyncTask task = new GetAllAsyncTask(dao);
//        task.execute();
//        try {
//            return task.get();
//        } catch (ExecutionException | InterruptedException e) {
//            return null;
//        }
//    }
// --Commented out by Inspection STOP (10.02.2021 15:22)

// --Commented out by Inspection START (10.02.2021 15:31):
//    private static final class GetAllAsyncTask extends AsyncTask<Void, Void, LiveData<List<Day>>> {
//        private final DayDao dao;


//        public GetAllAsyncTask(DayDao dao) {
//            this.dao = dao;
//        }


//        @Override
//        protected LiveData<List<Day>> doInBackground(Void... voids) {
//            return dao.getAll();
//        }
//    }
// --Commented out by Inspection STOP (10.02.2021 15:31)
}
