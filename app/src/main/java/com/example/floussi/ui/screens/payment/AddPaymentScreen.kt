package com.example.floussi.ui.screens.payment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.floussi.ui.screens.payment.components.NumericKeypad
import com.example.floussi.ui.theme.SuccessGreen
import com.example.floussi.ui.theme.VibrantTeal
import com.example.floussi.util.HapticType
import com.example.floussi.util.QRCodeGenerator
import com.example.floussi.util.rememberHapticFeedback
import com.example.floussi.util.toCurrency

@Composable
fun AddPaymentScreen(
    viewModel: AddPaymentViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hapticFeedback = rememberHapticFeedback()

    // Handle success state
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            hapticFeedback.performHapticFeedback(HapticType.SUCCESS)
            onSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(targetState = uiState.flowState, label = "payment_flow") { state ->
            when (state) {
                PaymentFlowState.INPUT -> {
                    InputContent(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = onBack,
                        hapticFeedback = hapticFeedback
                    )
                }
                PaymentFlowState.SHOW_QR -> {
                    QRCodeContent(
                        uiState = uiState,
                        onBack = onBack // Allow cancelling
                    )
                }
                PaymentFlowState.PAYMENT_RECEIVED -> {
                    PaymentReceivedContent()
                }
                PaymentFlowState.COMPLETED -> {
                    Box(modifier = Modifier.fillMaxSize()) // Handled by LaunchedEffect
                }
            }
        }
    }
}

@Composable
fun InputContent(
    uiState: AddPaymentUiState,
    viewModel: AddPaymentViewModel,
    onBack: () -> Unit,
    hapticFeedback: com.example.floussi.util.HapticFeedbackManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New Payment",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticType.CLICK)
                onBack()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Amount Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Amount",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.amount.ifEmpty { "0" }} MAD",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp
                ),
                color = VibrantTeal
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Note Field
        OutlinedTextField(
            value = uiState.note,
            onValueChange = { viewModel.updateNote(it) },
            label = { Text("Note (optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibrantTeal,
                focusedLabelColor = VibrantTeal
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Split Payment Control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Split Payment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledIconButton(
                    onClick = { 
                        hapticFeedback.performHapticFeedback(HapticType.CLICK)
                        viewModel.decrementSplit() 
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    enabled = uiState.splitCount > 1
                ) {
                    Icon(Icons.Default.Remove, "Decrease split")
                }

                Text(
                    text = "${uiState.splitCount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                FilledIconButton(
                    onClick = { 
                        hapticFeedback.performHapticFeedback(HapticType.CLICK)
                        viewModel.incrementSplit() 
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    enabled = uiState.splitCount < 10
                ) {
                    Icon(Icons.Default.Add, "Increase split")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Numeric Keypad
        NumericKeypad(
            hapticFeedback = hapticFeedback,
            onDigitClick = { digit -> viewModel.appendDigit(digit) },
            onDeleteClick = { viewModel.deleteDigit() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Charge Button
        Button(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticType.HEAVY_CLICK)
                viewModel.processPayment()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VibrantTeal,
                contentColor = Color.White
            ),
            enabled = !uiState.isProcessing && uiState.amount.toDoubleOrNull() != null && uiState.amount.toDouble() > 0
        ) {
            if (uiState.isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send Charge",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Error Message
        AnimatedVisibility(visible = uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QRCodeContent(
    uiState: AddPaymentUiState,
    onBack: () -> Unit
) {
    var qrBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    val splitAmount = (uiState.amount.toDoubleOrNull() ?: 0.0) / uiState.splitCount
    
    LaunchedEffect(uiState.currentSplitIndex, uiState.amount) {
        // Generate QR for the split amount
        // In a real app, this would include transaction ID, etc.
        val data = "pay:${splitAmount}:${System.currentTimeMillis()}"
        qrBitmap = QRCodeGenerator.generateQRCode(data, 512)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (uiState.splitCount > 1) "Payment ${uiState.currentSplitIndex + 1} of ${uiState.splitCount}" else "Scan to Pay",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = splitAmount.toCurrency("MAD"),
            style = MaterialTheme.typography.titleLarge,
            color = VibrantTeal
        )

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                qrBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: CircularProgressIndicator(color = VibrantTeal)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Waiting for payment...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(onClick = onBack) {
            Text("Cancel")
        }
    }
}

@Composable
fun PaymentReceivedContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = SuccessGreen.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Payment Received",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = SuccessGreen
        )
    }
}
