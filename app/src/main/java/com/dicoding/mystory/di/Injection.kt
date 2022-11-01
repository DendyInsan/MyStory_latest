package com.dicoding.mystory.di

import android.content.Context
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.data.PagingDataSource
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.repository.StoryRepository

object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val pagingDataSource = PagingDataSource.getPaging(database, apiService)
        return StoryRepository( apiService, pagingDataSource)
    }
}