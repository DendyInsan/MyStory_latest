package com.dicoding.mystory.view.signup


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.data.RegisterResponse
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.data.UserBody
import com.dicoding.mystory.model.UserPreference
import com.dicoding.mystory.repository.DataRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel (private val pref: UserPreference) : ViewModel(){

    fun register(Name:String, Email:String, Password:String ) = pref.registering(Name,Email,Password)


}