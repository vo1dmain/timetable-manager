package com.vo1d.schedulemanager.v2.data.courses;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.LinkedList;
import java.util.List;

public class CourseTypesConverter {
    @TypeConverter
    public String fromCourseTypes(CourseTypes... types) {
        List<String> strings = new LinkedList<>();

        for (CourseTypes t :
                types) {
            strings.add(t.name());
        }

        return TextUtils.join(",", strings);
    }

    @TypeConverter
    public CourseTypes[] toCourseTypes(String data) {
        if (data == null) {
            return null;
        }
        String[] strings = data.split(",");
        List<CourseTypes> types = new LinkedList<>();

        for (String s :
                strings) {
            types.add(CourseTypes.valueOf(s));
        }

        return types.toArray(new CourseTypes[0]);
    }
}
