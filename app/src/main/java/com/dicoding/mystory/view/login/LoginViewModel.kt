package com.dicoding.mystory.view.login
import androidx.lifecycle.*
import com.dicoding.mystory.api.ApiConfig
import com.dicoding.mystory.data.LoginResponse
import com.dicoding.mystory.data.SignInBody
import com.dicoding.mystory.model.UserModel
import com.dicoding.mystory.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun login(Email:String, Password:String ) = pref.logining(Email,Password)



}