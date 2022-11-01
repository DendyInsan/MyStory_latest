package com.dicoding.mystory.view.login
import androidx.lifecycle.*
import com.dicoding.mystory.model.UserPreference


class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun login(Email:String, Password:String ) = pref.logining(Email,Password)



}