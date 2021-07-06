package com.development.sota.scooter.net

import com.development.sota.scooter.ui.map.data.Rate
import com.development.sota.scooter.ui.map.data.Scooter
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface MapService {
    @GET("getScooter?status=ON&alert_status=OK")
    fun getActiveScooters(): Observable<List<Scooter>>

    @GET("getScooter?status=ON&alert_status=OK")
    fun getRentScooters(): Observable<List<Scooter>>

    @GET("getScooter")
    fun getAllScooters(): Observable<List<Scooter>>

    @GET("getRate/")
    fun getRate(): Observable<List<Rate>>

    @GET("getGeoZone/")
    fun getGeoZone(): Observable<String>

    @GET("getScooter/")
    fun getScooterByCode(@Query("id") idScooter: Long): Observable<ArrayList<Scooter>>
}