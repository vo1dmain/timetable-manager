package com.vo1d.schedulemanager.v2.ui.instructors.list;

import android.app.Application;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.data.instructors.InstructorWithJunctions;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

public class InstructorsListViewModel extends ListFragmentViewModel<InstructorWithJunctions> {

    public InstructorsListViewModel(@NonNull Application application) {
        super(application);
    }
}
