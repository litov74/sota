package com.development.sota.scooter.ui.promo.domain.entities

import com.google.gson.annotations.SerializedName

data class PromoData(
    @SerializedName("cost") val cost: Int,
    @SerializedName("id") val id: Long,
    @SerializedName("income") val income: Int
){
    fun getReplenishmentPackages(){

    }
}

data class PromoDataResponse(val data: List<PromoData>)

