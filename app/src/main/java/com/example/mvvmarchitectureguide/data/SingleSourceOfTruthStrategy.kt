package com.example.mvvmarchitectureguide.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import com.example.mvvmarchitectureguide.data.Result.Status.SUCCESS
import com.example.mvvmarchitectureguide.data.Result.Status.ERROR

fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == ERROR) {
            emit(Result.error<T>(responseStatus.message!!))
            emitSource(source)
        }
    }