package com.dicoding.mystory.view.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory.di.MapInjection
import com.dicoding.mystory.repository.DataRepository

class MapsViewModel2 (private val dataRepository: DataRepository) : ViewModel() {


    fun getAllStoryMap() = dataRepository.getAllStoryMap()
}

class ViewModelFactoryMap (private val dataRepository: DataRepository) : ViewModelProvider.NewInstanceFactory()  {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryMap? = null
        fun getInstance(context: Context): ViewModelFactoryMap =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactoryMap(MapInjection.provideDataRepository(context))
            }.also { INSTANCE= it }

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel2::class.java)) {
            return MapsViewModel2(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}