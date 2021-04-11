package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.ClientPurseResponse
import com.development.sota.scooter.ui.purse.domain.entities.TransactionsInfo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetClientTransactions {
    @GET("getTransactions/")
    fun fetchTransactionsInfo(@Query("id") id: Long): Observable<TransactionsInfo>
}
