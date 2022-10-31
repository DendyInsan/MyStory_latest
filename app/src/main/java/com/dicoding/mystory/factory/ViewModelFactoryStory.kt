package com.dicoding.mystory.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory.di.Injection
import com.dicoding.mystory.view.addstory.AddViewModel
import com.dicoding.mystory.view.main.MainViewModel2


class ViewModelFactoryStory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel2::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel2(Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddViewModel(Injection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}