package com.dicoding.mystory.view.main


import androidx.lifecycle.*
import com.dicoding.mystory.model.UserModel
import com.dicoding.mystory.model.UserPreference
import kotlinx.coroutines.launch


class MainViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}