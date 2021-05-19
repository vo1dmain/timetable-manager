package com.vo1d.schedulemanager.v2.data.instructors;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.BaseRepository;
import com.vo1d.schedulemanager.v2.data.ScheduleDb;

import java.util.List;
import java.util.concurrent.ExecutionException;

class InstructorRepository extends BaseRepository<Instructor, InstructorDao> {

    InstructorRepository(Application application) {
        super(ScheduleDb.getInstance(application).instructorDao());
    }

    void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    InstructorWithJunctions findById(int id) {
        FindByIdAsyncTask task = new FindByIdAsyncTask(dao);
        task.execute(id);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<InstructorWithJunctions> getFiltered(String filter) {
        GetFilteredAsyncTask task = new GetFilteredAsyncTask(dao);
        task.execute(filter);
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    LiveData<List<InstructorWithJunctions>> getAll() {
        GetAllAsyncTask task = new GetAllAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<InstructorWithJunctions> getAllAsList() {
        GetAllAsListAsyncTask task = new GetAllAsListAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    List<InstructorMinimised> getAllMinimisedAsList() {
        GetAllMinimisedAsListAsyncTask task = new GetAllMinimisedAsListAsyncTask(dao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private static final class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final InstructorDao dao;

        public DeleteAllAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static final class FindByIdAsyncTask extends AsyncTask<Integer, Void, InstructorWithJunctions> {
        private final InstructorDao dao;

        public FindByIdAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected InstructorWithJunctions doInBackground(Integer... integers) {
            return dao.findById(integers[0]);
        }
    }

    private static final class GetFilteredAsyncTask extends AsyncTask<String, Void, List<InstructorWithJunctions>> {
        private final InstructorDao dao;

        private GetFilteredAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<InstructorWithJunctions> doInBackground(String... strings) {
            return dao.getFiltered(strings[0]);
        }
    }

    private static final class GetAllAsyncTask extends AsyncTask<Void, Void, LiveData<List<InstructorWithJunctions>>> {
        private final InstructorDao dao;

        public GetAllAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<InstructorWithJunctions>> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    private static final class GetAllAsListAsyncTask extends AsyncTask<Void, Void, List<InstructorWithJunctions>> {
        private final InstructorDao dao;

        public GetAllAsListAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<InstructorWithJunctions> doInBackground(Void... voids) {
            return dao.getAllAsList();
        }
    }

    private static final class GetAllMinimisedAsListAsyncTask extends AsyncTask<Void, Void, List<InstructorMinimised>> {
        private final InstructorDao dao;

        public GetAllMinimisedAsListAsyncTask(InstructorDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<InstructorMinimised> doInBackground(Void... voids) {
            return dao.getAllMinimisedAsList();
        }
    }
}
