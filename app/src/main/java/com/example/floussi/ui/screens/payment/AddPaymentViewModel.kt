package com.example.floussi.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PaymentFlowState {
    INPUT,
    SHOW_QR,
    PAYMENT_RECEIVED,
    COMPLETED
}

data class AddPaymentUiState(
    val amount: String = "",
    val note: String = "",
    val splitCount: Int = 1,
    val currentSplitIndex: Int = 0,
    val flowState: PaymentFlowState = PaymentFlowState.INPUT,
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AddPaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddPaymentUiState())
    val uiState: StateFlow<AddPaymentUiState> = _uiState.asStateFlow()

    fun updateAmount(newAmount: String) {
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun updateNote(newNote: String) {
        _uiState.value = _uiState.value.copy(note = newNote)
    }

    fun incrementSplit() {
        val current = _uiState.value.splitCount
        if (current < 10) {
            _uiState.value = _uiState.value.copy(splitCount = current + 1)
        }
    }

    fun decrementSplit() {
        val current = _uiState.value.splitCount
        if (current > 1) {
            _uiState.value = _uiState.value.copy(splitCount = current - 1)
        }
    }

    fun appendDigit(digit: String) {
        if (_uiState.value.flowState != PaymentFlowState.INPUT) return
        
        val currentAmount = _uiState.value.amount
        val newAmount = if (currentAmount == "0") digit else currentAmount + digit
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun deleteDigit() {
        if (_uiState.value.flowState != PaymentFlowState.INPUT) return

        val currentAmount = _uiState.value.amount
        val newAmount = if (currentAmount.length > 1) {
            currentAmount.dropLast(1)
        } else {
            "0"
        }
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun processPayment() {
        val amount = _uiState.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.value = _uiState.value.copy(error = "Invalid amount")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProcessing = true, 
                error = null,
                currentSplitIndex = 0
            )

            val splits = _uiState.value.splitCount
            
            for (i in 0 until splits) {
                // Show QR Code
                _uiState.value = _uiState.value.copy(
                    flowState = PaymentFlowState.SHOW_QR,
                    currentSplitIndex = i,
                    isProcessing = false
                )
                
                // Wait 10 seconds
                delay(10000)
                
                // Show Payment Received
                _uiState.value = _uiState.value.copy(
                    flowState = PaymentFlowState.PAYMENT_RECEIVED
                )
                
                // Wait 2 seconds to show success message
                delay(2000)
            }

            // Completed
            _uiState.value = _uiState.value.copy(
                flowState = PaymentFlowState.COMPLETED,
                isSuccess = true
            )
        }
    }

    fun resetState() {
        _uiState.value = AddPaymentUiState()
    }
}
