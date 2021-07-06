package com.development.sota.scooter.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.SotaApp
import com.development.sota.scooter.ui.login.presentation.LoginActivity
import okhttp3.Interceptor
import java.io.IOException
import java.util.*

class UnauthorizedInterceptor() : Interceptor {

    var sharedPreferences: SharedPreferences = SotaApp.applicationContext().getSharedPreferences("account", Context.MODE_PRIVATE)

    private fun isNowLoginScreen(): Boolean {
        val am = SotaApp.applicationContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = Objects.requireNonNull(am).getRunningTasks(1)[0].topActivity
        return cn!!.className.compareTo(LoginActivity::class.java.getName()) == 0
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            Log.w("UnauthorizedInterceptor", "401 response")

            if (!isNowLoginScreen()) {
                sharedPreferences.edit().putString("token", "").apply()
                val intent = Intent(SotaApp.applicationContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                SotaApp.applicationContext().startActivity(intent)
            }
        }

        return response
    }
}