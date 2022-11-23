package ru.vo1d.ttmanager.data.entities.weeks

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vo1d.ttmanager.data.BaseDao

@Dao
interface WeeksDao : BaseDao<Week> {
    @get:Query("SELECT * FROM weeks ORDER BY id ASC")
    val all: Flow<List<Week>>
}