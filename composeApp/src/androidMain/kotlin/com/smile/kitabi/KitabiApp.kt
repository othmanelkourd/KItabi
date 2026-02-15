package com.smile.kitabi

import android.app.Application
import com.smile.kitabi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KitabiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KitabiApp)
            modules(appModule)
        }
    }
}