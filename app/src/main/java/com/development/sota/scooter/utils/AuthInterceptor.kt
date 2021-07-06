package com.development.sota.scooter.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.development.sota.scooter.SotaApp
import okhttp3.Interceptor
import java.io.IOException

class AuthInterceptor() : Interceptor {

     var sharedPreferences: SharedPreferences = SotaApp.applicationContext().getSharedPreferences("account", Context.MODE_PRIVATE)


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        var token = sharedPreferences.getString("token","")
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "$token")
            .build()
        return chain.proceed(newRequest)

//        var request = chain.request()
//
//
//
//
//
//        Log.w("AuthInterceptor", "token "+token)
//        if (token!!.length > 0) {
//            val newReq = chain.request().newBuilder()
//                .addHeader("Authorization", ""+token)
//                .build()
//            return chain.proceed(newReq)
//        }
//        return chain.proceed(request)
    }
}