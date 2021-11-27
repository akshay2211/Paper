package io.ak1.writedown.di

import android.content.Context
import androidx.room.Room
import io.ak1.writedown.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by akshay on 27/10/21
 * https://ak1.io
 */

/**
 * definitions for dependency injection for all selected classes
 */
/*
fun getLogInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }
}

fun returnRetrofit(interceptor: HttpLoggingInterceptor): Retrofit {
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    return Retrofit.Builder().baseUrl(ApiList.BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build()
}

fun getApi(retrofit: Retrofit): ApiList {
    return retrofit.create(ApiList::class.java)
}
*/


fun getDb(context: Context): AppDatabase {
    return synchronized(context) {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-writedown"
        ).build()
    }
}

fun getNoteTableDao(appDatabase: AppDatabase) = appDatabase.noteDao()
fun getFolderTableDao(appDatabase: AppDatabase) = appDatabase.folderDao()

fun getCoroutineContext(): CoroutineContext {
    return Dispatchers.IO
}
