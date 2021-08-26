package com.church.sdahymnal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.myatminsoe.mdetect.MDetect
import timber.log.Timber
import timber.log.Timber.DebugTree


@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MDetect.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}