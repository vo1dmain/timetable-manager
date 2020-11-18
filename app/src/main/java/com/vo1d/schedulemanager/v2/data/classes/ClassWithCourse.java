package com.vo1d.schedulemanager.v2.data.classes;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.courses.Course;
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;

import java.util.Objects;

public class ClassWithCourse implements IMyEntity {
    @Embedded
    public Class aClass;

    @Relation(parentColumn = "courseId", entityColumn = "id")
    public Course course;

    @Relation(parentColumn = "instructorId", entityColumn = "id")
    public Instructor instructor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassWithCourse)) return false;
        ClassWithCourse that = (ClassWithCourse) o;
        return aClass.equals(that.aClass) &&
                Objects.equals(course, that.course) &&
                Objects.equals(instructor, that.instructor);
    }
}
