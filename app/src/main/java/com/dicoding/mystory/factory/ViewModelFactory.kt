package com.dicoding.mystory.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory.di.UserInjection
import com.dicoding.mystory.model.UserPreference
import com.dicoding.mystory.view.addstory.AddStoryViewModel
import com.dicoding.mystory.view.detailview.DetailViewModel
import com.dicoding.mystory.view.login.LoginViewModel
import com.dicoding.mystory.view.main.MainViewModel
import com.dicoding.mystory.view.signup.SignUpViewModel
import com.dicoding.mystory.widget.WidgetUserViewModel


class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(WidgetUserViewModel::class.java) -> {
                WidgetUserViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
               DetailViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(UserInjection.providePreferences(context))
            }.also { instance = it }
    }

}