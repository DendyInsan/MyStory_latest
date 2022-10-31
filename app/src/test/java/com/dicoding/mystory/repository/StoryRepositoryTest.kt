package com.dicoding.mystory.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import com.dicoding.mystory.adapter.StoryListAdapter
import com.dicoding.mystory.api.ApiService
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.FakeApi
import com.dicoding.mystory.data.PagingDataSource
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.database.StoryDatabase
import com.dicoding.mystory.util.MainDispatcherRule
import com.dicoding.mystory.util.PagedTestDataSource
import com.dicoding.mystory.view.signup.getOrAwaitValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyRepository: StoryRepository
    private lateinit var apiService: ApiService
    private val dataDummy = DataDummy.storyFromDb()
    private val dummyToken = "XSJJDNdkdhduwN"
    private val dummyLocation :Int = 0
    private val context=mock(Context::class.java)
    private lateinit var pagingDataSource: PagingDataSource



    @Before
    fun setUp()
    {
        apiService = FakeApi()
        storyDatabase = Room.inMemoryDatabaseBuilder(context, StoryDatabase::class.java).build()
       storyRepository = StoryRepository(storyDatabase,apiService)
    }
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `When getStory Success `() = runTest {
        val dtDummy = dataDummy
        val expectedStories = MutableLiveData<PagingData<StoryResponseDB>>()
        expectedStories.value = PagedTestDataSource.snapshot(dtDummy)
        `when`(pagingDataSource.getStories(dummyToken,dummyLocation)).thenReturn(expectedStories)

        storyRepository.getStory(dummyToken,0).observeForever {
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
                advanceUntilIdle()
                Mockito.verify(pagingDataSource).getStories(dummyToken,dummyLocation)
                assertNotNull(differ.snapshot())
                assertEquals(differ.snapshot().size, dataDummy.size)
            }

        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}


