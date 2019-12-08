package com.example.mvvmarchitectureguide.data.user

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmarchitectureguide.data.resultLiveData
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val userRemoteDataSource: UserRemoteDataSource
) {

    fun observePagedUsers(
        connectivityAvailable: Boolean,
        coroutineScope: CoroutineScope
    ) =
        if (connectivityAvailable) observeRemoteUsers(coroutineScope)
        else observeLocalUsers()

    private fun observeLocalUsers(): LiveData<PagedList<User>> {
        val dataSourceFactory = dao.getPagedUsers()


        return LivePagedListBuilder(dataSourceFactory,
            UserPageDataSourceFactory.pagedListConfig()).build()
    }

    private fun observeRemoteUsers(ioCoroutineScope: CoroutineScope): LiveData<PagedList<User>> {
        val dataSourceFactory = UserPageDataSourceFactory(userRemoteDataSource,
            dao, ioCoroutineScope)
        return LivePagedListBuilder(dataSourceFactory,
            UserPageDataSourceFactory.pagedListConfig()).build()
    }

    suspend fun fillUsersListForTesting(){
        val users: List<User> = listOf(
            User("0", "Sam", "running, hiking, reading"),
            User("1", "Jess", "gaming, boxing, climbing"),
            User("2", "Brianna", "karate, partying, watching tv"),
            User("3", "Sarah", "skydiving, reading, working"),
            User("4", "Mary", "skiing, snowboarding, bungee jumping")
        )
        dao.insertAll(users)
    }

    fun observeUsers() = resultLiveData(
        databaseQuery = { dao.getUsers() },
        networkCall = { userRemoteDataSource.fetchUsers() },
        saveCallResult = { dao.insertAll(it.results) }
    )

    companion object {

        const val PAGE_SIZE = 100

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(dao: UserDao, userRemoteDataSource: UserRemoteDataSource) =
                instance ?: synchronized(this) {
                    instance ?: UserRepository(dao, userRemoteDataSource).also { instance = it }
                }
    }
}