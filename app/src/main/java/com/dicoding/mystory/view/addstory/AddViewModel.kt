package com.dicoding.mystory.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.mystory.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun addStory(token:String, photo: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody) =
        storyRepository.storyAdd(token,photo,description,lat,lon)
}