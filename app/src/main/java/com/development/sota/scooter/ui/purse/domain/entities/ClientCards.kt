package com.development.sota.scooter.ui.purse.domain.entities

import java.util.*


data class Card(val serverId: Long? = null,
                val dateTime: Date? = null,
                val cost: String? = null,
                val transactionId: Long? = null,
                val verified: Boolean? = null,
                val id: Long? = null,
                val card: String? = null,
                val card_id: Long? = null,
                val PaRes: String? = null,
                val crypto: String? = null,
                val is_main: Boolean? = null,
                val first_six: String? = null,
                val last_four: String? = null)



data class ReplenishmentPacks(val id: Long, val cost: Int, val income: Int)

