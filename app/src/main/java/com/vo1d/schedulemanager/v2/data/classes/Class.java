package com.vo1d.schedulemanager.v2.data.classes;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.days.Day;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypes;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypesConverter;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "class_table",
        foreignKeys = {
                @ForeignKey(entity = Subject.class, parentColumns = "id", childColumns = "subjectId", onDelete = CASCADE),
                @ForeignKey(entity = Day.class, parentColumns = "id", childColumns = "dayId", onDelete = CASCADE)
        },
        indices = {
                @Index(value = "id", unique = true),
                @Index(value = "dayId"),
                @Index(value = "subjectId")
        })
public class Class implements IMyEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int subjectId;
    public int dayId;

    public int audienceBuilding;
    public int audienceCabinet;
    public int startTimeHour;
    public int startTimeMinutes;
    public int endTimeHour;
    public int endTimeMinutes;

    @TypeConverters({SubjectTypesConverter.class})
    private SubjectTypes[] type;

    public Class(int subjectId, int dayId, int audienceBuilding, int audienceCabinet, SubjectTypes... type) {
        this.subjectId = subjectId;
        this.dayId = dayId;
        this.type = type;
        this.audienceBuilding = audienceBuilding;
        this.audienceCabinet = audienceCabinet;
    }

    public String getAudienceInfo() {
        return (String.valueOf(audienceBuilding) + '-' + audienceCabinet);
    }

    public String getStartTimeAsString() {
        String hour = String.valueOf(startTimeHour);
        String minute = String.valueOf(startTimeMinutes);

        if (hour.length() == 1) {
            hour = '0' + hour;
        }

        if (minute.length() == 1) {
            minute = '0' + minute;
        }

        return hour + ':' + minute;
    }

    public String getEndTimeAsString() {
        String hour = String.valueOf(endTimeHour);
        String minute = String.valueOf(endTimeMinutes);

        if (hour.length() == 1) {
            hour = '0' + hour;
        }

        if (minute.length() == 1) {
            minute = '0' + minute;
        }

        return hour + ':' + minute;
    }

    public SubjectTypes[] getType() {
        return this.type;
    }

    public void setType(SubjectTypes... type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return id == aClass.id &&
                subjectId == aClass.subjectId &&
                dayId == aClass.dayId &&
                startTimeHour == aClass.startTimeHour &&
                startTimeMinutes == aClass.startTimeMinutes &&
                endTimeHour == aClass.endTimeHour &&
                endTimeMinutes == aClass.endTimeMinutes &&
                type == aClass.type;
    }

}
