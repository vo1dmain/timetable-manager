package com.vo1d.schedulemanager.v2.data.courseinstructor;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.courses.Course;
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;

@Entity(tableName = "course_instructor_table",
        primaryKeys = {"courseId", "instructorId"},
        indices = {
                @Index(value = "courseId"),
                @Index(value = "instructorId")
        },
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "id",
                        childColumns = "courseId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Instructor.class,
                        parentColumns = "id",
                        childColumns = "instructorId",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class CourseInstructor implements IMyEntity {
    public int courseId;
    public int instructorId;

    public CourseInstructor(int courseId, int instructorId) {
        this.courseId = courseId;
        this.instructorId = instructorId;
    }
}
