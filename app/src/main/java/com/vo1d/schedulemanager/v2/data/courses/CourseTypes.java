package com.vo1d.schedulemanager.v2.data.courses;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.R;

public enum CourseTypes {
    Lecture,
    Practice,
    Laboratory;

    private static String[] names;

    public static void setResources(Resources res) {
        names = res.getStringArray(R.array.course_types);
    }

    @NonNull
    @Override
    public String toString() {
        return names[this.ordinal()];
    }
}
