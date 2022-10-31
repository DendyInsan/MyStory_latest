package com.dicoding.mystory.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object UserInjection {
    fun providePreferences(context: Context): UserPreference {
        val apiSvc = ApiConfig.getApiService()
        return UserPreference.getInstance(context.dataStore, apiSvc)
    }
}