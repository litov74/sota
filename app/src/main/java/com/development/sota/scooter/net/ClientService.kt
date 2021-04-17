package com.development.sota.scooter.net

import com.development.sota.scooter.ui.map.data.BookingBlockResponse
import com.development.sota.scooter.ui.map.data.Client
import com.development.sota.scooter.ui.profile.domain.entities.ClientUpdateNameData
import com.development.sota.scooter.ui.profile.domain.entities.ClientUpdateNamePhone
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ClientService {
    @GET("getClient")
    fun getClient(@Query("id") id: String): Observable<List<Client>>

    @GET("isClientBooksBlocked")
    fun checkBookingBlock(@Query("id") id: String): Observable<BookingBlockResponse>

    @POST("setClientName/")
    fun updateClientName(@Body jsonObject: ClientUpdateNameData): Observable<Response<Void>>

    @POST("setClientPhone/")
    fun updateClientPhone(@Body jsonObject: ClientUpdateNamePhone): Observable<Response<Void>>
}