package com.vo1d.schedulemanager.v2.ui.lecturers.list;

import android.app.Application;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.data.lecturers.Lecturer;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

public class LecturersListViewModel extends ListFragmentViewModel<Lecturer> {

    public LecturersListViewModel(@NonNull Application application) {
        super(application);
    }
}
