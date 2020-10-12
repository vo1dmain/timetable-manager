package com.vo1d.schedulemanager.v2.data.subjects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.Arrays;

@Entity(tableName = "subject_table", indices = @Index(value = {"id"}, unique = true))
public class Subject implements IMyEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String lecturerName;

    @TypeConverters({SubjectTypesConverter.class})
    private SubjectTypes[] subjectTypes;

    public Subject(String title, String lecturerName, SubjectTypes... subjectTypes) {
        this.title = title;
        this.lecturerName = lecturerName;
        this.subjectTypes = subjectTypes;
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

            boolean c1 = (this.id == s2.id);

            boolean c2 = (this.title.contentEquals(s2.title));

            boolean c3 = (this.lecturerName.contentEquals(s2.lecturerName));

            boolean c4 = (Arrays.equals(this.subjectTypes, s2.subjectTypes));

            return c1 && c2 && c3 && c4;
        } else {
            return obj.equals(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return title + " (" + lecturerName + ")";
    }
}
