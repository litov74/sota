package com.development.sota.scooter.ui.profile.domain.entities

data class ClientDataItem(
    val balance: String,
    val bank_card: String,
    val client_group: List<Any>,
    val client_name: String,
    val client_photo: String,
    val comment: String,
    val failed_books: Int,
    val id: Int,
    val phone: String,
    val status: String,
    val surname: String
)