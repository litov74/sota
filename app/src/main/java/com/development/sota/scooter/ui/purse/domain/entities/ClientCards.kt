package com.development.sota.scooter.ui.purse.domain.entities

import java.util.*


data class Card(val serverId: Long,
                val dateTime: Date,
                val cost: String,
                val transactionId: Long,
                val verified: Boolean,
                val id: Long,
                val card: String,
                val card_id: Long)