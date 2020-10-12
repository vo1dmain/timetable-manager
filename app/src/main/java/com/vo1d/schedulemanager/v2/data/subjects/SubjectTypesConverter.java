package com.vo1d.schedulemanager.v2.data.subjects;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.LinkedList;
import java.util.List;

public class SubjectTypesConverter {
    @TypeConverter
    public String fromSubjectTypes(SubjectTypes... types) {
        List<String> strings = new LinkedList<>();

        for (SubjectTypes t :
                types) {
            strings.add(t.name());
        }

        return TextUtils.join(",", strings);
    }

    @TypeConverter
    public SubjectTypes[] toSubjectTypes(String data) {
        String[] strings = data.split(",");
        List<SubjectTypes> types = new LinkedList<>();

        for (String s :
                strings) {
            types.add(SubjectTypes.valueOf(s));
        }

        return types.toArray(new SubjectTypes[0]);
    }
}
