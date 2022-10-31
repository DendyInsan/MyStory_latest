package com.dicoding.mystory.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.mystory.adapter.StoryListAdapter
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.StoryResponseDB
import com.dicoding.mystory.repository.StoryRepository
import com.dicoding.mystory.util.MainDispatcherRule
import com.dicoding.mystory.util.PagedTestDataSource.Companion.snapshot
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModel2Test {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var viewModel: MainViewModel2
    private val dataDummy = DataDummy.storyFromDb()
    private val dummyToken = "XSJJDNdkdhduwN"

    @Before
    fun setUp() {
        viewModel= MainViewModel2(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `sending, when get Story Success`() = runTest {
        val data = snapshot(dataDummy)
        val expectedResponse : LiveData<PagingData<StoryResponseDB>> = liveData {
            emit(data)
        }
        Mockito.`when`(storyRepository.getStory(dummyToken,0)).thenReturn( expectedResponse  )

        viewModel.sending(dummyToken).observeForever {
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
                advanceUntilIdle()
                Mockito.verify(storyRepository).getStory(dummyToken,0)
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