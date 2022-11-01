package com.dicoding.mystory.view.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.data.RegisterResponse
import com.dicoding.mystory.model.UserPreference
import com.dicoding.mystory.model.Result
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var  pref: UserPreference
    private lateinit var signupViewModel: SignUpViewModel
    private val dataDummy = DataDummy.ApiResponseSucces()
    private val dummyEmail = "bulux123@test.co.id"
    private val dummyPassword = "12345678"
    private val dummyName = "Bulux"

    @Before
    fun setUp() {

        signupViewModel = SignUpViewModel(pref)
           }


    @Test
    fun `when Register Success`()   {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success (dataDummy)

        Mockito.`when`(signupViewModel.register(  dummyName,dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        val actualResponse =signupViewModel.register(dummyName,dummyEmail,dummyPassword).getOrAwaitValue()

        Mockito.verify(pref).registering(dummyName,dummyEmail,dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dataDummy, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Register Failed Should Return Error`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Error("Error")
        Mockito.`when`(signupViewModel.register(dummyName,dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = signupViewModel.register(dummyName,dummyEmail,dummyPassword).getOrAwaitValue()

        Mockito.verify(pref).registering(dummyName,dummyEmail,dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

}