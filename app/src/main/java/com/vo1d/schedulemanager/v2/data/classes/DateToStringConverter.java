package com.vo1d.schedulemanager.v2.data.classes;


import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateToStringConverter {

    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    @TypeConverter
    public String fromDate(Date date) {
        if (date == null)
            return "null";
        return formatter.format(date);
    }

    @TypeConverter
    public Date toDate(String string) {
        if (string == null) {
            return new Date();
        } else {
            try {
                return formatter.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
    }
}
