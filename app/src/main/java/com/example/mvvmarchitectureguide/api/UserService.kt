package com.example.mvvmarchitectureguide.api

import com.example.mvvmarchitectureguide.data.user.User
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    companion object {
        const val ENDPOINT = "https://google.com"
    }

    @GET("users")
    suspend fun getUsers(): Response<ResultsResponse<User>>
}