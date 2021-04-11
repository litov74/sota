package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.AddCard
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface AddClientCard {
    @POST("addClientCard/")
    fun AddCard(): Observable<AddCard>
}
