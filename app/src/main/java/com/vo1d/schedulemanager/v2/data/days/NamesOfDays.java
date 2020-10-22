package com.vo1d.schedulemanager.v2.data.days;

public enum NamesOfDays {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    public static NamesOfDays fromInt(int i) {
        return values()[i];
    }
}
