package com.example.floussi.ui.screens.phonepayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PhonePaymentUiState(
    val phoneNumber: String = "",
    val countryCode: String = "+212",
    val amount: String = "",
    val note: String = "",
    val isSmsSent: Boolean = false,
    val showCodeEntry: Boolean = false,
    val verificationCode: String = "",
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class PhonePaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PhonePaymentUiState())
    val uiState: StateFlow<PhonePaymentUiState> = _uiState.asStateFlow()

    fun updatePhoneNumber(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(phoneNumber = digitsOnly)
    }

    fun updateCountryCode(code: String) {
        _uiState.value = _uiState.value.copy(countryCode = code)
    }

    fun updateVerificationCode(code: String) {
        _uiState.value = _uiState.value.copy(verificationCode = code.filter { it.isDigit() })
    }

    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    fun appendDigit(digit: String) {
        val current = _uiState.value.amount
        if (digit == "." && current.contains(".")) return

        val newAmount = when {
            current.isEmpty() -> digit
            current == "0" && digit != "." -> digit
            else -> current + digit
        }
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun deleteDigit() {
        val current = _uiState.value.amount
        val newAmount = when {
            current.isEmpty() -> ""
            current.length == 1 -> ""
            else -> current.dropLast(1)
        }
        _uiState.value = _uiState.value.copy(amount = newAmount)
    }

    fun sendRequest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProcessing = true,
                error = null,
                isSuccess = false,
                isSmsSent = false,
                showCodeEntry = false,
                verificationCode = ""
            )

            delay(1200)

            val amountValue = _uiState.value.amount.toDoubleOrNull()
            when {
                _uiState.value.phoneNumber.isBlank() -> {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        error = "Please enter a phone number"
                    )
                }

                amountValue == null || amountValue <= 0 -> {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        error = "Enter a valid amount"
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        isSmsSent = true,
                        showCodeEntry = false,
                        error = null
                    )
                }
            }
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProcessing = true,
                error = null
            )

            delay(1000)

            if (_uiState.value.verificationCode.length < 4) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    error = "Enter the code sent by SMS"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    isSuccess = true,
                    error = null
                )
            }
        }
    }

    fun revealCodeEntry() {
        _uiState.value = _uiState.value.copy(
            showCodeEntry = true,
            error = null
        )
    }

    fun reset() {
        _uiState.value = PhonePaymentUiState()
    }
}
