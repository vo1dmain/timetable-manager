package com.vo1d.schedulemanager.v2.ui.subjects.list;

import android.app.Application;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

public class SubjectsListViewModel extends ListFragmentViewModel<Subject> {

    public SubjectsListViewModel(@NonNull Application application) {
        super(application);
    }
}
