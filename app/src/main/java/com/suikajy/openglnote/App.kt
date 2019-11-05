package com.suikajy.openglnote

import android.app.Application
import com.suikajy.openglnote.util.AndroidStudioTree
import timber.log.Timber

/**
 * Created by suikajy on 2018.12.14
 */
lateinit var application: App

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        if (Config.DEBUG) {
            Timber.plant(AndroidStudioTree())
        }
    }
}
