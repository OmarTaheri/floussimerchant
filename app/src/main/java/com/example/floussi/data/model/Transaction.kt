package com.example.floussi.data.model

data class Transaction(
    val id: String,
    val amount: Double,
    val currency: String = "MAD",
    val customerName: String,
    val note: String,
    val timestamp: Long,
    val type: TransactionType,
    val status: TransactionStatus
)

enum class TransactionType {
    PAYMENT_RECEIVED,
    REFUND,
    WITHDRAWAL
}

enum class TransactionStatus {
    COMPLETED,
    PENDING,
    FAILED
}
