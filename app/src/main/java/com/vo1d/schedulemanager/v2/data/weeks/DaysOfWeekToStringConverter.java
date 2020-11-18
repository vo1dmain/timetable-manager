package com.vo1d.schedulemanager.v2.data.weeks;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.vo1d.schedulemanager.v2.data.days.DaysOfWeek;

import java.util.LinkedList;
import java.util.List;

public class DaysOfWeekToStringConverter {

    @TypeConverter
    public String fromDaysOfWeek(List<DaysOfWeek> data) {
        if (data != null) {
            if (data.isEmpty()) {
                return "";
            }

            List<String> strings = new LinkedList<>();

            for (DaysOfWeek t :
                    data) {
                strings.add(t.name());
            }

            return TextUtils.join(",", strings);
        } else return "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
    }

    @TypeConverter
    public List<DaysOfWeek> toDaysOfWeek(String data) {
        List<DaysOfWeek> types = new LinkedList<>();

        if (data == null) {
            return null;
        } else if (data.equals("")) {
            return types;
        }
        String[] strings = data.split(",");

        for (String s :
                strings) {
            types.add(DaysOfWeek.valueOf(s.trim()));
        }

        return types;
    }
}
