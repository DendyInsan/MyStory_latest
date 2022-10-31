package com.dicoding.mystory.model

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.data.*
import com.dicoding.mystory.view.main.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserPreference (
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
)
{
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?:"",
                preferences[NAME_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    fun registering(Name:String, Email:String, Password:String ): LiveData<Result<RegisterResponse>> = liveData  {
        emit(Result.Loading)
        try {
            val signUpInfo = UserBody(Name, Email, Password)
            val result = apiService.registeruser(signUpInfo)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("UserRepository", "Register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun logining(Email:String, Password:String ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val signInInfo = SignInBody(Email, Password)
            val result = apiService.signin(signInInfo)
            emit(Result.Success(result))
            if (result.error==false )
            {
                this@UserPreference.login()
                val logins = UserModel(result.loginResult?.token!!,result.loginResult?.name!!,true)
                this@UserPreference.saveUser(logins)
            }

        } catch (e: Exception) {
            Log.d("UserRepository", "Login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[NAME_KEY] = user.name
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
