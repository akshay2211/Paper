package com.pyrocodes.paper

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


class Paper : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}