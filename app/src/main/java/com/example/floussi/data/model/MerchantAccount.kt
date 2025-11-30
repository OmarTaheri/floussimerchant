package com.example.floussi.data.model

data class MerchantAccount(
    val merchantName: String,
    val accountType: String,
    val balance: Double,
    val currency: String = "MAD",
    val cardId: String,
    val avatarUrl: String? = null,
    val isActive: Boolean = true
)
