package com.vo1d.schedulemanager.v2.data.subject;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Arrays;

@Entity(tableName = "subject_table", indices = @Index(value = {"id"}, unique = true))
public class Subject {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String lecturerName;

    @TypeConverters({SubjectTypesConverter.class})
    private SubjectTypes[] subjectTypes;

    public Subject(String title, String lecturerName, SubjectTypes... subjectTypes) {
        this.title = title;
        this.lecturerName = lecturerName;
        this.subjectTypes = subjectTypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public SubjectTypes[] getSubjectTypes() {
        return subjectTypes;
    }

    public void setSubjectTypes(SubjectTypes... types) {
        this.subjectTypes = types;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            Subject s2 = (Subject) obj;

            boolean c1 = (this.id == s2.getId());

            boolean c2 = (this.title.contentEquals(s2.title));

            boolean c3 = (this.lecturerName.contentEquals(s2.lecturerName));

            boolean c4 = (Arrays.equals(this.subjectTypes, s2.subjectTypes));

            return c1 && c2 && c3 && c4;
        } else {
            return obj.equals(this);
        }
    }

    @Override
    public String toString() {
        return title + " (" + lecturerName + ")" ;
    }
}
