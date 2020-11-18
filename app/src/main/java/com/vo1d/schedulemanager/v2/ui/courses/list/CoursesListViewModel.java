package com.vo1d.schedulemanager.v2.ui.courses.list;

import android.app.Application;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

public class CoursesListViewModel extends ListFragmentViewModel<CourseWithInstructors> {

    public CoursesListViewModel(@NonNull Application application) {
        super(application);
    }
}
