package com.example.floussi.ui.screens.nfc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.floussi.util.NfcCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NfcStage {
    INITIAL,
    SIMULATING_CHECK,
    PAIRED_MESSAGE,
    SCANNING,
    PAYMENT_SUCCESS,
    // Legacy/Fallback states if needed, though we are overriding the flow
    CHECKING,
    NO_SUPPORT,
    DISABLED,
    READY,
    PAIRED,
    WAITING
}

data class NFCPaymentUiState(
    val isWaiting: Boolean = false,
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val isNFCAvailable: Boolean = true,
    val isNFCEnabled: Boolean = true,
    val amount: Double? = null,
    val amountInput: String = "",
    val cardData: String? = null,
    val statusMessage: String = "",
    val error: String? = null,
    val stage: NfcStage = NfcStage.INITIAL
)

class NFCPaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NFCPaymentUiState())
    val uiState: StateFlow<NFCPaymentUiState> = _uiState.asStateFlow()

    fun updateAmountInput(value: String) {
        val clean = value.filter { it.isDigit() || it == '.' }.take(10)
        _uiState.value = _uiState.value.copy(amountInput = clean)
    }

    // Called when "Get Payment" is clicked
    fun startPaymentFlow() {
        viewModelScope.launch {
            // 1. Show "NFC doesn't exist"
            _uiState.value = _uiState.value.copy(
                stage = NfcStage.SIMULATING_CHECK,
                statusMessage = "Checking NFC..."
            )
            delay(3000) // 3 seconds

            // 2. Show "App paired with NFC device"
            _uiState.value = _uiState.value.copy(
                stage = NfcStage.PAIRED_MESSAGE,
                statusMessage = "App paired with NFC device"
            )
            delay(3000) // 3 seconds

            // 3. Show "Scan your card" + Wave
            _uiState.value = _uiState.value.copy(
                stage = NfcStage.SCANNING,
                statusMessage = "Scan your card",
                isWaiting = true
            )
            delay(4000) // 4 seconds

            // 4. Payment Successful
            _uiState.value = _uiState.value.copy(
                stage = NfcStage.PAYMENT_SUCCESS,
                statusMessage = "Payment Successful",
                isSuccess = true,
                isWaiting = false
            )
        }
    }

    // Legacy/Helper methods to keep existing code compiling if referenced elsewhere or needed for actual NFC
    fun setNfcStatus(isAvailable: Boolean, isEnabled: Boolean) {
        // We might ignore actual status for this specific simulation flow, 
        // or just store it without changing the stage from INITIAL
        _uiState.value = _uiState.value.copy(
            isNFCAvailable = isAvailable,
            isNFCEnabled = isEnabled
        )
    }

    fun onCardDetected(card: NfcCard) {
        // In this simulation, we might not actually use this, 
        // but if a real card is tapped during SCANNING, we could fast-forward to success
        if (_uiState.value.stage == NfcStage.SCANNING) {
             _uiState.value = _uiState.value.copy(
                isWaiting = false,
                isProcessing = true,
                cardData = "UID: ${card.uid} • ${card.cardType}",
                statusMessage = "Processing card...",
                stage = NfcStage.PAYMENT_SUCCESS, // Jump to success
                isSuccess = true
            )
        }
    }

    fun onError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }

    fun resetForRetry() {
        _uiState.value = NFCPaymentUiState(stage = NfcStage.INITIAL)
    }
    
    fun markPairedAfterNoSupport() {} // No-op in new flow
    fun prepareWaitingIfReady() {} // No-op in new flow
    fun forceSuccess() {} // No-op in new flow
}
