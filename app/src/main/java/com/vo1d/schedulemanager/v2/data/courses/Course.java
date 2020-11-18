package com.vo1d.schedulemanager.v2.data.courses;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.Arrays;

@Entity(tableName = "course_table", indices = @Index(value = {"id"}, unique = true))
public class Course implements IMyEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;

    @TypeConverters({CourseTypesConverter.class})
    private CourseTypes[] courseTypes;

    public Course(String title, CourseTypes... courseTypes) {
        this.title = title;
        this.courseTypes = courseTypes;
    }

    public CourseTypes[] getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(CourseTypes... types) {
        this.courseTypes = types;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            Course s2 = (Course) obj;

            boolean c1 = (this.id == s2.id);

            boolean c2 = (this.title.contentEquals(s2.title));

            boolean c4 = (Arrays.equals(this.courseTypes, s2.courseTypes));

            return c1 && c2 && c4;
        } else {
            return obj.equals(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
