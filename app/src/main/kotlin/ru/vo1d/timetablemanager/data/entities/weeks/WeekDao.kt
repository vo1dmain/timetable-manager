package ru.vo1d.timetablemanager.data.entities.weeks

import androidx.room.Dao
import ru.vo1d.timetablemanager.data.BaseDao

@Dao
interface WeekDao : BaseDao<Int, Week>