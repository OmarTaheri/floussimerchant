package com.example.floussi.data.model

data class PaymentRequest(
    val amount: Double,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)
