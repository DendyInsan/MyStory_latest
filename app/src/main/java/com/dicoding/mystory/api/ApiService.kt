package com.dicoding.mystory.api



import com.dicoding.mystory.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("login")
    suspend  fun signin(
        @Body info: SignInBody
    ): LoginResponse

    @Headers("Content-Type:application/json")
    @POST("register")
    suspend fun registeruser(
              @Body info: UserBody
    ): RegisterResponse


    @Headers("Content-Type:application/json")
    @GET("stories")
    suspend fun getallstories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): StoryResponse


    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): FileUploadResponse

}

