package com.vo1d.schedulemanager.v2.data.courses;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorMinimised;

import java.util.List;

public class CourseWithInstructors implements IMyEntity {
    @Embedded
    public Course course;

    @Relation(parentColumn = "id",
            entity = Instructor.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = CourseInstructor.class,
                    parentColumn = "courseId",
                    entityColumn = "instructorId"))
    public List<InstructorMinimised> instructors;

    @Relation(parentColumn = "id",
            entity = CourseInstructor.class,
            entityColumn = "courseId")
    public List<CourseInstructor> junctions;

    @NonNull
    @Override
    public String toString() {
        return course.title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseWithInstructors)) return false;
        CourseWithInstructors that = (CourseWithInstructors) o;
        return course.equals(that.course) &&
                instructors.equals(that.instructors);
    }
}
