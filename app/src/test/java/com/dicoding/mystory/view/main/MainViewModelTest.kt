package com.dicoding.mystory.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.model.UserModel
import com.dicoding.mystory.model.UserPreference
import com.dicoding.mystory.util.MainCoroutineRule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Mock
    private lateinit var pref: UserPreference
    private lateinit var viewModel: MainViewModel
    private val dataDummy2 = UserModel("", "", false)
    private val dataDummy1 = DataDummy.generateGetUser()

    @Before
    fun setUp() {
        viewModel = MainViewModel(pref)
    }

    @Test
    fun `getUser success`() {
        val expectedResponse: Flow<UserModel> = flow { emit(dataDummy1) }

        Mockito.`when`(pref.getUser()).thenReturn(expectedResponse)
        viewModel.getUser().observeForever {
            Mockito.verify(pref).getUser()
            Assert.assertNotNull(it)
            Assert.assertEquals(dataDummy1, it)
        }
    }

    @Test
    fun `when get User Failed`() {
        val expectedResponse: Flow<UserModel> = flow { emit(dataDummy2) }

        Mockito.`when`(pref.getUser()).thenReturn(expectedResponse)
        viewModel.getUser().observeForever {
            Mockito.verify(pref).getUser()
            Assert.assertNotNull(it)
            Assert.assertEquals(dataDummy2, it)
        }

    }

    @Test
    fun `when logout Success`() = mainDispatcherRule.runBlockingTest{
        viewModel.logout()
        Mockito.verify(pref).logout()
    }
}