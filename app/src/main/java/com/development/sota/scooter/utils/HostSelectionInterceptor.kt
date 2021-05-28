package com.development.sota.scooter.utils

import android.content.Context
import android.content.SharedPreferences
import com.development.sota.scooter.BuildConfig
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.SotaApp
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class ChangeableBaseUrlInterceptor() : Interceptor {

    lateinit var sharedPreferences: SharedPreferences


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()

        sharedPreferences = SotaApp.applicationContext().getSharedPreferences("account", Context.MODE_PRIVATE)

        var host = sharedPreferences.getString("host",BuildConfig.API_DNS)?.toHttpUrlOrNull()

        host?.let {
            val newUrl = request.url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()
            request = request.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}