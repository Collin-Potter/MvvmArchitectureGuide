package com.example.mvvmarchitectureguide.dashboardmodule.ui

import androidx.lifecycle.ViewModel
import com.example.mvvmarchitectureguide.data.user.UserRepository
import com.example.mvvmarchitectureguide.di.CoroutineScopeIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val repository: UserRepository,
    @CoroutineScopeIO private val ioCoroutineScopeIO: CoroutineScope
)
    : ViewModel() {


    var connectivityAvailable: Boolean = false
    val users by lazy {
        repository.observePagedUsers(
            connectivityAvailable, ioCoroutineScopeIO
        )
    }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScopeIO.cancel()
    }

    suspend fun fillList() {
        repository.fillUsersListForTesting()
    }
}