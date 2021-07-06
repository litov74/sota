package com.development.sota.scooter

import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import net.danlew.android.joda.JodaTimeInitializer

class SotaApp : MultiDexApplication() {

    init {
        instance = this
    }

    var context: Context = this

    companion object {
        private var instance: SotaApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        val context: Context = SotaApp.applicationContext()
        AppInitializer.getInstance(this).initializeComponent(JodaTimeInitializer::class.java)
    }

}