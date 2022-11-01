package com.dicoding.mystory.data

import com.dicoding.mystory.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody


class FakeApi: ApiService {
    private val dummyLoginResponseSuccess =  DataDummy.generateDummyLoginResponseSuccess()
    private val dummyApiResponseSuccess = DataDummy.generateDummyApiResponseSuccess()
    private val dummyStoryResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyStoryUpRes = DataDummy.generateDummyStoryUploadResponseSuccess()
    override suspend fun signin(info:SignInBody): LoginResponse{
        return dummyLoginResponseSuccess
    }

    override suspend fun registeruser(info: UserBody): RegisterResponse {
        return  dummyApiResponseSuccess
    }

    override suspend fun getallstories(
         token: String,
        page: Int,
        size: Int,
         location: Int
    ): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun uploadImage(
        token:  String,
        file: MultipartBody.Part,
        description: RequestBody,
       lat: RequestBody,
       lon: RequestBody,
    ):FileUploadResponse {
        return dummyStoryUpRes
    }

}



