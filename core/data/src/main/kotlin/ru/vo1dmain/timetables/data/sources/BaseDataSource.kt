package ru.vo1dmain.timetables.data.sources

interface BaseDataSource<I, K> {
    suspend fun create(item: I): Int
    
    suspend fun findById(id: K): I?
    
    suspend fun update(item: I)
    
    suspend fun upsert(item: I): Int
    
    suspend fun delete(item: I)
}