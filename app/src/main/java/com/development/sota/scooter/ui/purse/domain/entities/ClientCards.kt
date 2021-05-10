package com.development.sota.scooter.ui.purse.domain.entities

import java.util.*


data class UserCardModel(val serverId: Long? = null,
                         val dateTime: Date? = null,
                         val cost: String? = null,
                         val transactionId: Long? = null,
                         val verified: Boolean? = null,
                         val id: Long? = null,
                         val card: String? = null,
                         val card_id: Long = 0,
                         val PaRes: String? = null,
                         val crypto: String? = null,
                         val is_main: Boolean? = null,
                         val first_six: String? = null,
                         val last_four: String? = null)

data class TransactionModel(
                val date_time: Date? = null,
                val cost: String? = null,
                val verified: Boolean? = null,
                val id: Long? = null,
                val used_card: String? = null,
                val receipt_link: String?)


data class ReplenishmentPacks(val id: Long, val cost: Int, val income: Int)

