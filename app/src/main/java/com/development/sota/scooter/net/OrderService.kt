package com.development.sota.scooter.net

import com.development.sota.scooter.ui.drivings.domain.entities.AddOrderResponse
import com.development.sota.scooter.ui.drivings.domain.entities.Order
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface OrderService {
    //TODO: Add "active" filter

    @GET("getOrder/")
    fun getOrders(@Query("client") clientId: Long): Observable<List<Order>>

    @POST("addOrder/")
    @FormUrlEncoded
    fun addOrder(
        @Field("start_time") startTime: String,
        @Field("scooter") scooterId: Long,
        @Field("client") clientId: Long
    ): Observable<AddOrderResponse>

    @POST("activateRent/")
    @FormUrlEncoded
    fun activateOrder(
        @Field("id") orderId: Long
    ): Observable<Response<Void>>

    @POST("closeOrder/")
    @FormUrlEncoded
    fun closeOrder(
        @Field("id") orderId: Long
    ): Observable<Response<JsonElement>>

    @POST("cancelOrder/")
    @FormUrlEncoded
    fun cancelOrder(
        @Field("id") orderId: Long
    ): Completable

    @POST("stopOrder/")
    @FormUrlEncoded
    fun pauseOrder(
            @Field("id") orderId: Long
    ): Completable

    @POST("executeScooterCommand/")
    @FormUrlEncoded
    fun openScooterLook(
        @Field("id") id: Long,
        @Field("command") command: String
    ): Completable

    @POST("resumeOrder/")
    @FormUrlEncoded
    fun resumeOrder(
            @Field("id") orderId: Long
    ): Completable


    @POST("setTaxType/")
    @FormUrlEncoded
    fun setRateType(
        @Field("order") orderId: Long,
        @Field("type") rateType: String
    ): Completable
}