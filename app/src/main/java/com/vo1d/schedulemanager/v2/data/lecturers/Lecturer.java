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
    public String phoneNumber;

    public Lecturer(String firstName, String middleName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber == null ? "null" : phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecturer lecturer = (Lecturer) o;
        return id == lecturer.id &&
                firstName.equals(lecturer.firstName) &&
                middleName.equals(lecturer.middleName) &&
                lastName.equals(lecturer.lastName) &&
                phoneNumber.equals(lecturer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, phoneNumber);
    }

    public String getShortName() {
        return lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + ".";
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }
}
