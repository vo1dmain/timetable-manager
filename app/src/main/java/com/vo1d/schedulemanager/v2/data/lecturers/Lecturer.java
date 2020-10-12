package com.vo1d.schedulemanager.v2.data.lecturers;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.Objects;

@Entity(tableName = "lecturer_table", indices = {@Index(value = {"id"}, unique = true)})
public class Lecturer implements IMyEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstName;
    public String middleName;
    public String lastName;

    public Lecturer(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecturer lecturer = (Lecturer) o;
        return id == lecturer.id &&
                firstName.equals(lecturer.firstName) &&
                middleName.equals(lecturer.middleName) &&
                lastName.equals(lecturer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName);
    }

    public String getShortName() {
        return lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + ".";
    }
}
