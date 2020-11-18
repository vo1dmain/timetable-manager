package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithCourse;
import com.vo1d.schedulemanager.v2.ui.ListFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassesListViewModel extends ListFragmentViewModel<ClassWithCourse> {

    private LiveData<List<ClassWithCourse>> allClassesForADay;

    public ClassesListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ClassWithCourse>> getAllClassesForADay() {
        return allClassesForADay;
    }

    public void setAllClassesForADay(LiveData<List<ClassWithCourse>> allClassesForADay) {
        this.allClassesForADay = allClassesForADay;
    }

    public Class[] getSelectedItemsAsClassArray(@NonNull Class[] array) {
        List<Class> classList = new ArrayList<>();
        Objects.requireNonNull(selectedItems.getValue())
                .forEach(classWithCourse -> classList.add(classWithCourse.aClass));
        return classList.toArray(array);
    }
}
