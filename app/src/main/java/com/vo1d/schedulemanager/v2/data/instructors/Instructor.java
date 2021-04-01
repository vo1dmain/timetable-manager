package com.vo1d.schedulemanager.v2.data.instructors;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.Objects;

@Entity(tableName = "instructor_table", indices = {@Index(value = {"id"}, unique = true)})
public class Instructor implements IMyEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstName;
    public String middleName;
    public String lastName;
    public String email;

    public Instructor(String firstName, String middleName, String lastName, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email == null ? "null" : email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instructor instructor = (Instructor) o;
        return id == instructor.id &&
                firstName.equals(instructor.firstName) &&
                middleName.equals(instructor.middleName) &&
                lastName.equals(instructor.lastName) &&
                email.equals(instructor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, email);
    }

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
