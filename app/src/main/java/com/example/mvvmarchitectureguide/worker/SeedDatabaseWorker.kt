package com.example.mvvmarchitectureguide.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mvvmarchitectureguide.data.AppDatabase
import com.example.mvvmarchitectureguide.data.user.User
import com.example.mvvmarchitectureguide.util.DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            try {
                applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<User>>() {}.type
                        val list: List<User> = Gson().fromJson(jsonReader, type)

                        AppDatabase.getInstance(applicationContext).userDao().insertAll(list)

                        Result.success()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error seeding database")
                Result.failure()
            }
        }
    }
}