package com.development.sota.scooter

import android.content.Context
import androidx.multidex.MultiDexApplication

class SotaApp : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: SotaApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        val context: Context = SotaApp.applicationContext()
    }

}