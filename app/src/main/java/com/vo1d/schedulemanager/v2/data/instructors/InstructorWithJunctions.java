package com.vo1d.schedulemanager.v2.data.instructors;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.courseinstructor.CourseInstructor;

import java.util.List;
import java.util.Objects;

public class InstructorWithJunctions implements IMyEntity {
    @Embedded
    public Instructor instructor;

    @Relation(entity = CourseInstructor.class,
            parentColumn = "id",
            entityColumn = "instructorId")
    public List<CourseInstructor> junctions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructorWithJunctions)) return false;
        InstructorWithJunctions that = (InstructorWithJunctions) o;
        return instructor.equals(that.instructor) &&
                Objects.equals(junctions, that.junctions);
    }
}
