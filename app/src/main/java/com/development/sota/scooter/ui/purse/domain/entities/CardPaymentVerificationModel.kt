package com.development.sota.scooter.ui.purse.domain.entities

data class CardPaymentVerificationModel (
        val TransactionId: Long,
        val PaReq: String,
        val GoReq: Any? = null,
        val AcsUrl: String,
        val IFrameIsAllowed: Boolean,
        val FrameWidth: Any? = null,
        val FrameHeight: Any? = null,
        val ThreeDsCallbackId: String,
        val EscrowAccumulationId: Any? = null
)