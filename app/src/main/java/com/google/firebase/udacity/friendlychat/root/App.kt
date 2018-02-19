package com.google.firebase.udacity.friendlychat.root

import android.app.Application

/**
 * Created by jorge_caro on 2/9/18.
 */


open class App : Application() {

    companion object {
        @JvmStatic lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}