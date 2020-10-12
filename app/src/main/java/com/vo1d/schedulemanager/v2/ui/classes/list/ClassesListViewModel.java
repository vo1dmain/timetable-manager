package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithSubject;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

import java.util.List;

public class ClassesListViewModel extends ListFragmentViewModel<Class> {

    private LiveData<List<ClassWithSubject>> allClassesForADay2;

    public ClassesListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ClassWithSubject>> getAllClassesForADay2() {
        return allClassesForADay2;
    }

    public void setAllClassesForADay2(LiveData<List<ClassWithSubject>> allClassesForADay) {
        this.allClassesForADay2 = allClassesForADay;
    }
}
