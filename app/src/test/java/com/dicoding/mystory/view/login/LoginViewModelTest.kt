package com.dicoding.mystory.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystory.data.LoginResponse
import com.dicoding.mystory.model.Result
import com.dicoding.mystory.model.UserPreference
import com.dicoding.mystory.data.DataDummy
import com.dicoding.mystory.view.signup.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var  pref: UserPreference
    private lateinit var loginViewModel: LoginViewModel
    private val dataDummy = DataDummy.LoginResponSuccess()
    private val dummyEmail = "bulux123@test.co.id"
    private val dummyPassword = "12345678"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(pref)
    }

    @Test
    fun `when Login Success`()   {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success (dataDummy)

        Mockito.`when`(loginViewModel.login( dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        val actualResponse =loginViewModel.login(dummyEmail,dummyPassword).getOrAwaitValue()
        Mockito.verify(pref).logining(dummyEmail,dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dataDummy, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Login Failed`()   {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value =  Result.Error("")

        Mockito.`when`(loginViewModel.login( dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail,dummyPassword).observeForever{result ->
            Mockito.verify(pref).logining(dummyEmail,dummyPassword)

            Assert.assertFalse(result is Result.Success)
            Assert.assertTrue(result is Result.Error)

        }


    }

}