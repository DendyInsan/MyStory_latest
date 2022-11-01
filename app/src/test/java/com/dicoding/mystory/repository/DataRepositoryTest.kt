package com.dicoding.mystory.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.room.Room
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.LocalDataSource
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.database.StoryDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var db:StoryDatabase
    private val dataDummy = DataDummy.storyFromDb()
    private val context = mock(Context::class.java)
    private lateinit var dataRepository: DataRepository


    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, StoryDatabase::class.java).build()
        dataRepository = DataRepository(localDataSource)
    }

    @Test
    fun `when get data from db success`() {
        val expectedResponse =MutableLiveData<List<StoryResponseDB>>()
        Mockito.`when`(localDataSource.getStories()).thenReturn(expectedResponse)
         dataRepository.getAllStoryMap().observeForever(){
             Mockito.verify(localDataSource).getStories()
             assertNotNull(it)
             assertTrue(it.isNotEmpty())
             assertEquals(it.size, dataDummy.size)
         }

    }
}