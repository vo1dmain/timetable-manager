package com.vo1d.schedulemanager.v2.data.days;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.vo1d.schedulemanager.v2.R;

public enum DaysOfWeek {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    private static String[] names;

    public static DaysOfWeek fromInt(int i) {
        return values()[i];
    }

    public static void setResources(Resources res) {
        names = res.getStringArray(R.array.days_of_week_full);
    }

    @NonNull
    @Override
    public String toString() {
        return names[this.ordinal()];
    }
}
