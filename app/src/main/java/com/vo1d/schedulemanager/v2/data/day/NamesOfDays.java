package com.vo1d.schedulemanager.v2.data.day;

public enum NamesOfDays {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    private static NamesOfDays[] allValues = values();

    public static NamesOfDays fromInt(int i)
    {
        return allValues[i];
    }
}
