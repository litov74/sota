package com.development.sota.scooter.ui.map.data

import com.google.gson.annotations.SerializedName

data class Client(
    val id: Long,
    @SerializedName("client_name") val clientName: String,
    val surname: String,
    val status: String,
    val balance: String,
    val bonus_balance: String,
    @SerializedName("client_photo") val clientPhoto: String?,
    val phone: String,
    @SerializedName("failed_books") val failedBooks: Long,
    @SerializedName("bank_card") val bankCard: String,
    val comment: String,
)
