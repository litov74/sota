package com.development.sota.scooter.net

import com.development.sota.scooter.ui.purse.domain.entities.RemoveClientCard
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoveClientCard {
    @POST("removeClientCard/")
    fun RemoveCard(): Observable<RemoveClientCard>
}
