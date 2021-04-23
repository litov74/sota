package com.development.sota.scooter.ui.purse.domain.entities

data class WrapperCardPaymentVerificationModel (
        val Model: CardPaymentVerificationModel,
        val Success: Boolean,
        val Message: Any? = null
)