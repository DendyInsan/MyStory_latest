package com.dicoding.mystory.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.mystory.model.UserPreference

class SignUpViewModel (private val pref: UserPreference) : ViewModel(){

    fun register(Name:String, Email:String, Password:String ) = pref.registering(Name,Email,Password)


}