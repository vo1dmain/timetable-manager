package com.vo1d.schedulemanager.v2.data.day;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.classes.Class;

import java.util.List;

public class DayWithClasses {

    //@Embedded
    public Day day;

    //@Relation(parentColumn = "id", entityColumn = "dayId")
    public List<Class> classes;
}
