package com.dicoding.mystory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.database.StoryDatabase

class PagingDataSource (
    private val storyDatabase: StoryDatabase, private val apiService: ApiService
) {
    fun getStories(token:String, location:Int): LiveData<PagingData<StoryResponseDB>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),

            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token, location),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }

        ).liveData
    }

    companion object {

        fun getPaging(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
        ): PagingDataSource {
            return PagingDataSource(storyDatabase, apiService)
        }
    }
}