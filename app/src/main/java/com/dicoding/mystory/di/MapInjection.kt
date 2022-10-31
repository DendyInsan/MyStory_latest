package com.dicoding.mystory.di

import android.app.Application
import android.content.Context
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.factory.ViewModelFactory.Companion.getInstance
import com.dicoding.mystory.repository.DataRepository
import com.dicoding.mystory.repository.StoryRepository

object MapInjection {
    fun provideDataRepository(context: Context): DataRepository{
        val database = StoryDatabase.getDatabase(context)

        return DataRepository.getInstance (database)
    }
}