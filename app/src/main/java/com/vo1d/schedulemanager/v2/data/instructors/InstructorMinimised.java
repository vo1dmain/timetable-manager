package com.vo1d.schedulemanager.v2.data.instructors;

import androidx.annotation.NonNull;

public class InstructorMinimised {
    public int id;

    public String firstName;
    public String middleName;
    public String lastName;

    public String getShortName() {
        return lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + ".";
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    @NonNull
    @Override
    public String toString() {
        return getFullName();
    }
}
