package com.dicoding.mystory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.repository.StoryRepository

class MainViewModel2(private val storyRepository: StoryRepository) : ViewModel() {

    fun sending(token:String): LiveData<PagingData<StoryResponseDB>>
    {
            return  storyRepository.getStory(token).cachedIn(viewModelScope)

    }
}

