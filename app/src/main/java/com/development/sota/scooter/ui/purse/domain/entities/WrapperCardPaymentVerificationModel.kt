package com.development.sota.scooter.ui.purse.domain.entities

import com.google.gson.JsonElement

data class WrapperCardPaymentVerificationModel (
        val Model: JsonElement,
        val Success: Boolean,
        val Message: Any? = null
)