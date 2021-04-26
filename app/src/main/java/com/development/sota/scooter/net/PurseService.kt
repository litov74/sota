package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.*
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface PurseService {

    @POST("removeClientCard/")
    @FormUrlEncoded
    fun RemoveCard(
        @Field("card") card: String,
        @Field("card_id") card_id: Long,
        @Field("id") id: Long
    ): Observable<UserCardModel>

    @POST("SetCardAsMain/")
    @FormUrlEncoded
    fun SetMain(
        @Field("card") card: String,
        @Field("id") id: Long
    ): Observable<UserCardModel>

    @POST("addClientCard/")
    @FormUrlEncoded
    fun AddCard(
        @Field("card") card: String,
        @Field("id") id: Long
    ): Observable<UserCardModel>

    @POST("confirmCardLinking")
    @FormUrlEncoded
    fun confirmCardLinking(
        @Field("MD") transactionId: Long,
        @Field("PaRes") PaRes: String
    ): Observable<UserCardModel>


    @GET("getClientCards/")
    fun getClientCards(@Query("id") id: Long): Observable<List<UserCardModel>>

    @GET("getTransactions/")
    fun fetchTransactionsInfo(@Query("id") id: Long): Observable<List<TransactionModel>>

    @GET("getReplenishmentPackages/")
    fun getReplenishmentPackages(): Observable<Response<List<UpBalancePackageModel>>>

    @POST("topUpBalance/")
    @FormUrlEncoded
    fun topUpBalance( @Field("id") clientId: Long,
                      @Field("package") packageId: Long): Observable<WrapperCardPaymentVerificationModel>

    @POST("SetCardAsMain/")
    @FormUrlEncoded
    fun setMainCard( @Field("id") clientId: Long,
                     @Field("card") card: String): Observable<JsonElement>

    @POST("removeClientCard/")
    @FormUrlEncoded
    fun deleteUserCard( @Field("id") clientId: Long, @Field("card") card: String, @Field("card_id") card_id: Long,): Observable<JsonElement>

}