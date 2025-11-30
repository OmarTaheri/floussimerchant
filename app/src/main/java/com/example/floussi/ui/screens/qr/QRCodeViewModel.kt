package com.example.floussi.ui.screens.qr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QRCodeUiState(
    val qrCodeBitmap: Bitmap? = null,
    val merchantId: String = "FLOUSSI_YEM_001",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showShareDialog: Boolean = false
)

class QRCodeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QRCodeUiState())
    val uiState: StateFlow<QRCodeUiState> = _uiState.asStateFlow()

    fun generateQRCode(qrGenerator: (String) -> Bitmap?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val merchantId = _uiState.value.merchantId
            val qrData = "floussi://merchant/${merchantId}"

            val bitmap = qrGenerator(qrData)

            _uiState.value = _uiState.value.copy(
                qrCodeBitmap = bitmap,
                isLoading = false
            )
        }
    }

    fun showShareDialog() {
        _uiState.value = _uiState.value.copy(showShareDialog = true)
    }

    fun hideShareDialog() {
        _uiState.value = _uiState.value.copy(showShareDialog = false)
    }
}
