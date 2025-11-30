package com.example.floussi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.floussi.data.model.MerchantAccount
import com.example.floussi.data.model.Transaction
import com.example.floussi.data.model.TransactionStatus
import com.example.floussi.data.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val merchantAccount: MerchantAccount = MerchantAccount(
        merchantName = "Youssef El-Mansouri",
        accountType = "Merchant Account",
        balance = 24500.00,
        cardId = "****1234",
        isActive = true
    ),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate loading recent transactions (replace with actual repository call)
            val mockTransactions = listOf(
                Transaction(
                    id = "1",
                    amount = 250.00,
                    customerName = "Ahmed Hassan",
                    note = "Coffee order",
                    timestamp = System.currentTimeMillis() - 3600000,
                    type = TransactionType.PAYMENT_RECEIVED,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "2",
                    amount = 1200.00,
                    customerName = "Fatima Zahra",
                    note = "Catering service",
                    timestamp = System.currentTimeMillis() - 7200000,
                    type = TransactionType.PAYMENT_RECEIVED,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "3",
                    amount = 450.00,
                    customerName = "Omar Bennani",
                    note = "Product purchase",
                    timestamp = System.currentTimeMillis() - 10800000,
                    type = TransactionType.PAYMENT_RECEIVED,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "4",
                    amount = 85.00,
                    customerName = "Karim Tazi",
                    note = "Breakfast",
                    timestamp = System.currentTimeMillis() - 14400000,
                    type = TransactionType.PAYMENT_RECEIVED,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "5",
                    amount = 1500.00,
                    customerName = "Salma Idrissi",
                    note = "Consulting fee",
                    timestamp = System.currentTimeMillis() - 86400000,
                    type = TransactionType.PAYMENT_RECEIVED,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "6",
                    amount = 300.00,
                    customerName = "Yassine Berrada",
                    note = "Refund",
                    timestamp = System.currentTimeMillis() - 90000000,
                    type = TransactionType.REFUND,
                    status = TransactionStatus.COMPLETED
                ),
                Transaction(
                    id = "7",
                    amount = 2000.00,
                    customerName = "Bank Withdrawal",
                    note = "ATM",
                    timestamp = System.currentTimeMillis() - 172800000,
                    type = TransactionType.WITHDRAWAL,
                    status = TransactionStatus.COMPLETED
                )
            )

            _uiState.value = _uiState.value.copy(
                transactions = mockTransactions,
                isLoading = false
            )
        }
    }

    fun refreshTransactions() {
        loadInitialData()
    }
}
