package com.dicoding.mystory.view.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.FileUploadResponse
import com.dicoding.mystory.model.Result
import com.dicoding.mystory.repository.StoryRepository
import com.dicoding.mystory.util.MainDispatcherRule
import com.dicoding.mystory.view.signup.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AddViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var viewModel: AddViewModel
    private val dataDummy = DataDummy.generateDummyStoryUploadResponseSuccess()
    private val dummyToken = "XSJJDNdkdhduwN"
    private var dummyMultipart = DataDummy.generateDummyMultipartFile()
    private var dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyResponseError = "error"
    private val dummyLat=DataDummy.generateDummylat()
    private val dummyLon=DataDummy.generateDummylon()

    @Before
    fun setUp() {
        viewModel= AddViewModel(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `add story, when Success`(){
        val expectedResponse = MutableLiveData<Result<FileUploadResponse>>()
        expectedResponse.value = Result.Success (dataDummy)
        Mockito.`when`(viewModel.addStory(dummyToken, dummyMultipart, dummyDescription,dummyLat, dummyLon))
            .thenReturn(
                expectedResponse
            )
        val actualResponse =viewModel.addStory(dummyToken, dummyMultipart, dummyDescription,dummyLat, dummyLon).getOrAwaitValue()

        Mockito.verify(storyRepository).storyAdd(dummyToken, dummyMultipart, dummyDescription,dummyLat, dummyLon)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dataDummy, (actualResponse as Result.Success).data)
    }
}