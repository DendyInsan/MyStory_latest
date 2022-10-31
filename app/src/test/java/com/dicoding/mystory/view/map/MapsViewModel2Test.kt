package com.dicoding.mystory.view.map


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.repository.DataRepository
import com.dicoding.mystory.util.MainDispatcherRule
import com.dicoding.mystory.view.signup.getOrAwaitValue
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
internal class MapsViewModel2Test {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository
     private lateinit var viewModel: MapsViewModel2
    private val dataDummy = DataDummy.storyFromDb()


    @Before
    fun setUp() {
      viewModel = MapsViewModel2(dataRepository)

    }
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Test
    fun `when get Story from local Success`()   {
        val expectedResponse : LiveData<List<StoryResponseDB>> = liveData { emit(dataDummy) }
         Mockito.`when`(viewModel.getAllStoryMap()).thenReturn(expectedResponse)
        viewModel.getAllStoryMap().observeForever {
            Mockito.verify(dataRepository).getAllStoryMap()
            Assert.assertNotNull(it)
            Assert.assertTrue(it.isNotEmpty())
            assertEquals(it.size, dataDummy.size)
        }

    }

    @Test
    fun `when get Story from local Failed`()   {
        val expectedResponse : LiveData<List<StoryResponseDB>> = liveData { emit(listOf()) }
        Mockito.`when`(viewModel.getAllStoryMap()).thenReturn(expectedResponse)
        viewModel.getAllStoryMap() .observeForever {
            Mockito.verify(dataRepository).getAllStoryMap()
            Assert.assertNotNull(it)
            Assert.assertTrue(it.isEmpty())
        }

    }


}