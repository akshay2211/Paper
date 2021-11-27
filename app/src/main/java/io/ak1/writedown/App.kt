package io.ak1.writedown

import android.app.Application
import io.ak1.writedown.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            //inject Android context
            androidContext(applicationContext)
            koin.loadModules(
                listOf(
                    databaseModule
                )
            )
        }
    }
}