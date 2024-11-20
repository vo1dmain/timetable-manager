package ru.vo1dmain.timetables.data.entities.week

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vo1dmain.timetables.data.BaseDao

@Dao
interface WeeksDao : BaseDao<Week> {
    @get:Query("SELECT * FROM weeks ORDER BY id ASC")
    val all: Flow<List<Week>>
}