package com.development.sota.scooter.ui.login.domain.entities



data class WrapperDns (

    val AD: Boolean,

    val Answer: List<Answer>,

    val CD: Boolean,

    val Comment: String,

    val Question: List<Question>,

    val RA: Boolean,

    val RD: Boolean,

    val Status: Long,

    val TC: Boolean
)

data class Answer (

    val TTL: Long,

    val data: String,
    val name: String,
    val type: Long
)

data class Question (
    val name: String,
    val type: Long
)