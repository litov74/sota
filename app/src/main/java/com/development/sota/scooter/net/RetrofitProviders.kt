package com.development.sota.scooter.net

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.development.sota.scooter.SotaApp
import com.google.gson.Gson
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val gson = Gson()
val interceptor = LoggingInterceptor()

const val BASE_URL = "http://www.scooteradminpanel.ru/API/"

class HeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newReq = chain.request().newBuilder()
            .addHeader("User-Agent", "Sota Dev Scooter Android Web Connector")
            .build()
        return chain.proceed(newReq)
    }
}

val client: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(HeadersInterceptor())
    .addInterceptor(interceptor)
    .addInterceptor(ChuckerInterceptor(SotaApp.applicationContext()))
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(client)
    .build()

val jsonlessRetfofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
    .client(client)
    .build()

object LoginRetrofitProvider {
    val service: LoginService = retrofit.create(LoginService::class.java)
}

object MapRetrofitProvider {
    val service: MapService = retrofit.create(MapService::class.java)
    val jsonlessService: MapService = jsonlessRetfofit.create(MapService::class.java)
}

object OrdersRetrofitProvider {
    val service: OrderService = retrofit.create(OrderService::class.java)
}

object ClientRetrofitProvider {
    val service: ClientService = retrofit.create(ClientService::class.java)
}


object GetReplenishmentPackagesProvider{
    val service: GetReplenishmentPackages = retrofit.create(GetReplenishmentPackages::class.java)
}



object PurseRetrofitProvider{
    val service: PurseService = retrofit.create(PurseService::class.java)
}