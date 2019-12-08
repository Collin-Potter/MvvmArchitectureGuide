package com.example.mvvmarchitectureguide.data.user

import com.example.mvvmarchitectureguide.api.BaseDataSource
import com.example.mvvmarchitectureguide.api.UserService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(private val service: UserService) : BaseDataSource() {

    suspend fun fetchUsers() = getResult { service.getUsers() }

}