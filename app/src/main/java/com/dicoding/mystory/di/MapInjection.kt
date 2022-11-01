package com.dicoding.mystory.di

import android.content.Context
import com.dicoding.mystory.data.LocalDataSource
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.repository.DataRepository

object MapInjection {
    fun provideDataRepository(context: Context): DataRepository{
        val dao = StoryDatabase.getDatabase(context).storyDao()
        val localDataSource = LocalDataSource.getMap(dao)
        return DataRepository.getInstance (localDataSource)
    }


}