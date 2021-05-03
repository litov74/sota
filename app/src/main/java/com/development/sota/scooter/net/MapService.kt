package com.development.sota.scooter.net

import com.development.sota.scooter.ui.map.data.Rate
import com.development.sota.scooter.ui.map.data.Scooter
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface MapService {
    @GET("getScooter/")
    fun getScooters(): Observable<List<Scooter>>

    @GET("getRate/")
    fun getRate(): Observable<List<Rate>>

    @GET("getGeoZone/")
    fun getGeoZone(): Observable<String>


    @GET("getScooter/")
    fun getScooterByCode(@Query("id") idScooter: Long): Observable<ArrayList<Scooter>>
}