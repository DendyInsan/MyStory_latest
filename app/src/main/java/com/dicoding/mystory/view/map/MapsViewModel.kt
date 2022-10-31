package com.dicoding.mystory.view.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.di.Injection
import com.dicoding.mystory.di.MapInjection
import com.dicoding.mystory.di.UserInjection
import com.dicoding.mystory.factory.ViewModelFactory
import com.dicoding.mystory.repository.DataRepository

class MapsViewModel2 (private val dataRepository: DataRepository) : ViewModel() {


    fun getAllStoryMap() = dataRepository.getAllStoryMap()
}

//class ViewModelFactoryMap private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory()  {
class ViewModelFactoryMap (private val dataRepository: DataRepository) : ViewModelProvider.NewInstanceFactory()  {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryMap? = null
        fun getInstance(context: Context): ViewModelFactoryMap =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactoryMap(MapInjection.provideDataRepository(context))
            }.also { INSTANCE= it }
//        @JvmStatic
//        fun getInstance(application: Application): ViewModelFactoryMap {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactoryMap::class.java) {
//                    INSTANCE = ViewModelFactoryMap(application)
//                }
//            }
//            return INSTANCE as ViewModelFactoryMap
//        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel2::class.java)) {
            return MapsViewModel2(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}