package com.example.mvvmarchitectureguide.data.user

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UserPageDataSourceFactory @Inject constructor(
    private val dataSource: UserRemoteDataSource,
    private val dao: UserDao,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, User>() {

    private val liveData = MutableLiveData<UserPageDataSource>()

    override fun create(): DataSource<Int, User> {
        val source = UserPageDataSource(dataSource, dao, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 100

        fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}