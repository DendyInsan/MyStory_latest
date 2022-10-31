package com.dicoding.mystory.util

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.mystory.data.StoryResponseDB

class PagedTestDataSource :
    PagingSource<Int, LiveData<List<StoryResponseDB>>>() {

    companion object {
        fun snapshot(items: List<StoryResponseDB>): PagingData<StoryResponseDB> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponseDB>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponseDB>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}