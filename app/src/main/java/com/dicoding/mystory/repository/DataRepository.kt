package com.dicoding.mystory.repository

import androidx.lifecycle.LiveData
import com.dicoding.mystory.data.LocalDataSource
import com.dicoding.mystory.data.StoryResponseDB

class DataRepository (private val localDataSource:LocalDataSource){

     fun getAllStoryMap():LiveData<List<StoryResponseDB>> {
         return localDataSource.getStories()
     }

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(localDataSource:LocalDataSource): DataRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataRepository(localDataSource)
                INSTANCE = instance
                instance
            }
        }
    }


}