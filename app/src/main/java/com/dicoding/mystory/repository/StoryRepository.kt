package com.dicoding.mystory.repository

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.data.FileUploadResponse
import com.dicoding.mystory.data.StoryRemoteMediator
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.model.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {


    fun getStory(token:String, location:Int): LiveData<PagingData<StoryResponseDB>> {

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

    fun storyAdd(token:String, photo: MultipartBody.Part,  description: RequestBody, lat:RequestBody, lon:RequestBody): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService .uploadImage("Bearer $token", photo, description,lat,lon)
            emit(Result.Success(result))
        } catch (e: Exception) {

            emit(Result.Error(e.message.toString()))
        }

    }

companion object {
    @Volatile
    private var INSTANCE: StoryRepository? = null

    fun getInstance(
        storyDatabase: StoryDatabase,
        apiService: ApiService
    ): StoryRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = StoryRepository(storyDatabase, apiService)
            INSTANCE = instance
            instance
        }
    }
}
}

