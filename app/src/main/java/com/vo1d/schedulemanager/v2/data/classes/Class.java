package com.vo1d.schedulemanager.v2.data.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.courses.Course;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypesConverter;
import com.vo1d.schedulemanager.v2.data.days.Day;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "class_table",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId", onDelete = CASCADE),
                @ForeignKey(entity = Day.class, parentColumns = "id", childColumns = "dayId", onDelete = CASCADE)
        },
        indices = {
                @Index(value = "id", unique = true),
                @Index(value = "dayId"),
                @Index(value = "courseId"),
                @Index(value = "instructorId")
        })
public class Class implements IMyEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(defaultValue = "0")
    public int instructorId;
    public int courseId;
    public final int dayId;
    public int audienceBuilding;
    public int audienceCabinet;

    @TypeConverters({DateToStringConverter.class})
    @ColumnInfo(defaultValue = "00:00")
    public Date startTime;

    @TypeConverters({DateToStringConverter.class})
    @ColumnInfo(defaultValue = "00:00")
    public Date endTime;

    @TypeConverters({CourseTypesConverter.class})
    private CourseTypes[] type;

    public Class(int courseId,
                 int dayId,
                 int instructorId,
                 int audienceBuilding,
                 int audienceCabinet,
                 CourseTypes... type) {
        this.courseId = courseId;
        this.dayId = dayId;
        this.instructorId = instructorId;
        this.type = type;
        this.audienceBuilding = audienceBuilding;
        this.audienceCabinet = audienceCabinet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Class)) return false;
        Class aClass = (Class) o;
        return id == aClass.id &&
                courseId == aClass.courseId &&
                dayId == aClass.dayId &&
                audienceBuilding == aClass.audienceBuilding &&
                audienceCabinet == aClass.audienceCabinet &&
                startTime.equals(aClass.startTime) &&
                endTime.equals(aClass.endTime) &&
                Arrays.equals(type, aClass.type);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, courseId, dayId, audienceBuilding, audienceCabinet, startTime, endTime);
        result = 31 * result + Arrays.hashCode(type);
        return result;
    }

    public String getAudienceInfo() {
        return (String.valueOf(audienceBuilding) + '-' + audienceCabinet);
    }

    public CourseTypes[] getType() {
        return this.type;
    }

    public void setType(CourseTypes... type) {
        this.type = type;
    }
}
