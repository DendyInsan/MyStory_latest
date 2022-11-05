package com.dicoding.mystory.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.data.FileUploadResponse
import com.dicoding.mystory.data.PagingDataSource
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.model.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService, private val pagingDataSource: PagingDataSource) {


    fun getStory(token:String, location:Int): LiveData<PagingData<StoryResponseDB>> {

        return pagingDataSource.getStories(token, location)
    }

    fun storyAdd(token:String, photo: MultipartBody.Part,  description: RequestBody, lat:RequestBody?, lon:RequestBody?): LiveData<Result<FileUploadResponse>> = liveData {
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

        apiService: ApiService,
        pagingDataSource: PagingDataSource
    ): StoryRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = StoryRepository( apiService,pagingDataSource)
            INSTANCE = instance
            instance
        }
    }
}
}

