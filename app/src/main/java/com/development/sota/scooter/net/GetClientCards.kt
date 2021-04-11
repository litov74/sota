package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.AddCard
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers

interface GetClientCards {
    @GET("getClientCards/")
    fun fetchPurseInfo()
}
