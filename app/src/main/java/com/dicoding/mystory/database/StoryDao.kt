package com.dicoding.mystory.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.mystory.data.StoryResponseDB

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryResponseDB>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryResponseDB>

    @Query("SELECT * FROM story WHERE lon is not null and lat is not null")
    fun getAllStoryMap(): LiveData<List<StoryResponseDB>>

    @Query("DELETE FROM story")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)
}