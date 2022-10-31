package com.dicoding.mystory.repository

import android.app.Application
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.mystory.data.StoryResponse
import com.dicoding.mystory.model.Result
import com.dicoding.mystory.data.StoryResponseDB

import com.dicoding.mystory.database.StoryDatabase



class DataRepository (private val db:StoryDatabase){


    fun getAllStoryMap(): LiveData<List<StoryResponseDB>> =db.storyDao().getAllStoryMap()


    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(db: StoryDatabase): DataRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataRepository(db)
                INSTANCE = instance
                instance
            }
        }
    }


}