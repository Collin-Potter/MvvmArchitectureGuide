package com.example.mvvmarchitectureguide.di

import android.app.Application
import com.example.mvvmarchitectureguide.BuildConfig
import com.example.mvvmarchitectureguide.api.AuthInterceptor
import com.example.mvvmarchitectureguide.api.UserService
import com.example.mvvmarchitectureguide.data.AppDatabase
import com.example.mvvmarchitectureguide.data.user.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideUserService(
        @UserAPI okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, UserService::class.java)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userService: UserService) =
            UserRemoteDataSource(userService)

    @UserAPI
    @Provides
    fun providePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_DEVELOPER_TOKEN)).build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UserService.ENDPOINT)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}