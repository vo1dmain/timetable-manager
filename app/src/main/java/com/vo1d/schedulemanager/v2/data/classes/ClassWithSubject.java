package com.vo1d.schedulemanager.v2.data.classes;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.vo1d.schedulemanager.v2.data.IMyEntity;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;

public class ClassWithSubject implements IMyEntity {
    @Embedded
    public Class aClass;

    @Relation(parentColumn = "subjectId", entityColumn = "id")
    public Subject subject;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassWithSubject that = (ClassWithSubject) o;
        return aClass.equals(that.aClass) &&
                subject.equals(that.subject);
    }
}
