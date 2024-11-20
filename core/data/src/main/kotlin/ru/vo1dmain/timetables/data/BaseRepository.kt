package ru.vo1dmain.timetables.data


abstract class BaseRepository<I : DatabaseEntity, D : BaseDao<I>> {
    protected abstract val dao: D
    
    suspend fun insert(item: I) =
        dao.insert(item)
    
    suspend fun insert(vararg items: I) =
        dao.insert(*items)
    
    suspend fun update(item: I) =
        dao.update(item)
    
    suspend fun delete(vararg items: I) =
        dao.delete(*items)
}