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
                val PaRes: String? = null)
