package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.*

interface PurseService {

    @POST("removeClientCard/")
    @FormUrlEncoded
    fun RemoveCard(
        @Field("card") card: String,
        @Field("card_id") card_id: Long,
        @Field("id") id: Long
    ): Observable<Card>

    @POST("SetCardAsMain/")
    @FormUrlEncoded
    fun SetMain(
        @Field("card") card: String,
        @Field("id") id: Long
    ): Observable<Card>

    @POST("addClientCard/")
    @FormUrlEncoded
    fun AddCard(
        @Field("card") card: String,
        @Field("id") id: Long
    ): Observable<Card>

    @POST("confirmCardLinking")
    @FormUrlEncoded
    fun confirmCardLinking(
        @Field("MD") transactionId: Long,
        @Field("PaRes") PaRes: String
    ): Observable<Card>


    @GET("getClientCards/")
    fun fetchPurseInfo(@Query("id") id: Long): Observable<List<Card>>

    @GET("getTransactions/")
    fun fetchTransactionsInfo(@Query("id") id: Long): Observable<List<TransactionModel>>



}