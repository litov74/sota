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

//data class CardPaymentVerificationModel (
//        @Json(name = "ReasonCode")
//        val reasonCode: Long,
//
//        @Json(name = "PublicId")
//        val publicID: String,
//
//        @Json(name = "TerminalUrl")
//        val terminalURL: String,
//
//        @Json(name = "TransactionId")
//        val transactionID: Long,
//
//        @Json(name = "Amount")
//        val amount: Long,
//
//        @Json(name = "Currency")
//        val currency: String,
//
//        @Json(name = "CurrencyCode")
//        val currencyCode: Long,
//
//        @Json(name = "PaymentAmount")
//        val paymentAmount: Long,
//
//        @Json(name = "PaymentCurrency")
//        val paymentCurrency: String,
//
//        @Json(name = "PaymentCurrencyCode")
//        val paymentCurrencyCode: Long,
//
//        @Json(name = "InvoiceId")
//        val invoiceID: Any? = null,
//
//        @Json(name = "AccountId")
//        val accountID: String,
//
//        @Json(name = "Email")
//        val email: Any? = null,
//
//        @Json(name = "Description")
//        val description: String,
//
//        @Json(name = "JsonData")
//        val jsonData: Any? = null,
//
//        @Json(name = "CreatedDate")
//        val createdDate: String,
//
//        @Json(name = "PayoutDate")
//        val payoutDate: Any? = null,
//
//        @Json(name = "PayoutDateIso")
//        val payoutDateISO: Any? = null,
//
//        @Json(name = "PayoutAmount")
//        val payoutAmount: Any? = null,
//
//        @Json(name = "CreatedDateIso")
//        val createdDateISO: String,
//
//        @Json(name = "AuthDate")
//        val authDate: Any? = null,
//
//        @Json(name = "AuthDateIso")
//        val authDateISO: Any? = null,
//
//        @Json(name = "ConfirmDate")
//        val confirmDate: Any? = null,
//
//        @Json(name = "ConfirmDateIso")
//        val confirmDateISO: Any? = null,
//
//        @Json(name = "AuthCode")
//        val authCode: Any? = null,
//
//        @Json(name = "TestMode")
//        val testMode: Boolean,
//
//        @Json(name = "Rrn")
//        val rrn: Any? = null,
//
//        @Json(name = "OriginalTransactionId")
//        val originalTransactionID: Any? = null,
//
//        @Json(name = "FallBackScenarioDeclinedTransactionId")
//        val fallBackScenarioDeclinedTransactionID: Any? = null,
//
//        @Json(name = "IpAddress")
//        val ipAddress: String,
//
//        @Json(name = "IpCountry")
//        val ipCountry: String,
//
//        @Json(name = "IpCity")
//        val ipCity: String,
//
//        @Json(name = "IpRegion")
//        val ipRegion: String,
//
//        @Json(name = "IpDistrict")
//        val ipDistrict: String,
//
//        @Json(name = "IpLatitude")
//        val ipLatitude: Double,
//
//        @Json(name = "IpLongitude")
//        val ipLongitude: Double,
//
//        @Json(name = "CardFirstSix")
//        val cardFirstSix: String,
//
//        @Json(name = "CardLastFour")
//        val cardLastFour: String,
//
//        @Json(name = "CardExpDate")
//        val cardExpDate: String,
//
//        @Json(name = "CardType")
//        val cardType: String,
//
//        @Json(name = "CardProduct")
//        val cardProduct: String,
//
//        @Json(name = "CardCategory")
//        val cardCategory: String,
//
//        @Json(name = "EscrowAccumulationId")
//        val escrowAccumulationID: Any? = null,
//
//        @Json(name = "IssuerBankCountry")
//        val issuerBankCountry: String,
//
//        @Json(name = "Issuer")
//        val issuer: String,
//
//        @Json(name = "CardTypeCode")
//        val cardTypeCode: Long,
//
//        @Json(name = "Status")
//        val status: String,
//
//        @Json(name = "StatusCode")
//        val statusCode: Long,
//
//        @Json(name = "CultureName")
//        val cultureName: String,
//
//        @Json(name = "Reason")
//        val reason: String,
//
//        @Json(name = "CardHolderMessage")
//        val cardHolderMessage: String,
//
//        @Json(name = "Type")
//        val type: Long,
//
//        @Json(name = "Refunded")
//        val refunded: Boolean,
//
//        @Json(name = "Name")
//        val name: String,
//
//        @Json(name = "Token")
//        val token: Any? = null,
//
//        @Json(name = "SubscriptionId")
//        val subscriptionID: Any? = null,
//
//        @Json(name = "GatewayName")
//        val gatewayName: String,
//
//        @Json(name = "ApplePay")
//        val applePay: Boolean,
//
//        @Json(name = "AndroidPay")
//        val androidPay: Boolean,
//
//        @Json(name = "WalletType")
//        val walletType: String,
//
//        @Json(name = "TotalFee")
//        val totalFee: Long
//)