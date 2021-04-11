package com.development.sota.scooter.ui.purse.domain.entities

import java.util.*

data class ClientPurseResponse(val cardCryptogram: String, val cardId: Int, val code: Int)

data class RemoveClientCard(val cardCryptogram: String, val cardId: Int, val id: Long)

data class AddCard(val cardCryptogram: String, val id: Long)

data class TransactionsInfo(val serverId: Long, val dateTime: Date, val cost: String, val transactionId: Long, val verified: Boolean, val id: Long)

data class GetCards(val cardCryptogram: String, val isMain: Boolean, val cardId: Int)