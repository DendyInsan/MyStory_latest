package com.dicoding.mystory.repository

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.data.FileUploadResponse
import com.dicoding.mystory.data.PagingDataSource
import com.dicoding.mystory.data.StoryRemoteMediator
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.model.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(  private val pagingDataSource: PagingDataSource) {

    fun getStory(token: String): LiveData<PagingData<StoryResponseDB>> {
        return pagingDataSource.getStories(token, 0)
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
        pagingDataSource: PagingDataSource,

    ): StoryRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = StoryRepository(pagingDataSource)
            INSTANCE = instance
            instance
        }
    }
}
}

