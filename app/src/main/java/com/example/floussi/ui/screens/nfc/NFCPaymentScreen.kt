package com.example.floussi.ui.screens.nfc

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.floussi.ui.theme.GradientMiddle
import com.example.floussi.ui.theme.VibrantTeal
import com.example.floussi.util.HapticType
import com.example.floussi.util.rememberHapticFeedback

@Composable
fun NFCPaymentScreen(
    viewModel: NFCPaymentViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hapticFeedback = rememberHapticFeedback()

    // Handle success navigation
    LaunchedEffect(uiState.stage) {
        if (uiState.stage == NfcStage.PAYMENT_SUCCESS) {
            hapticFeedback.performHapticFeedback(HapticType.SUCCESS)
            kotlinx.coroutines.delay(2000) // Show success for 2s then navigate
            onSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Close Button (Always visible except maybe in success)
        if (uiState.stage != NfcStage.PAYMENT_SUCCESS) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (uiState.stage) {
                NfcStage.INITIAL -> {
                    // 1. Amount Input Field
                    OutlinedTextField(
                        value = uiState.amountInput,
                        onValueChange = { viewModel.updateAmountInput(it) },
                        label = { Text("Amount (MAD)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantTeal,
                            focusedLabelColor = VibrantTeal
                        )
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // 2. Button of getting payment
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticType.CLICK)
                            viewModel.startPaymentFlow()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VibrantTeal
                        )
                    ) {
                        Text(
                            text = "Get Payment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                NfcStage.SIMULATING_CHECK -> {
                    // 3. Show "NFC doesn't exist"
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "NFC doesn't exist",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                NfcStage.PAIRED_MESSAGE -> {
                    // 4. Show "App paired with NFC device"
                    Icon(
                        imageVector = Icons.Default.Devices,
                        contentDescription = "Paired",
                        tint = VibrantTeal,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "App paired with NFC device",
                        style = MaterialTheme.typography.headlineSmall,
                        color = VibrantTeal,
                        textAlign = TextAlign.Center
                    )
                }

                NfcStage.SCANNING -> {
                    // 5. Show "Scan your card" + Wave
                    NFCWaveAnimation(
                        isActive = true,
                        modifier = Modifier.size(280.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Scan your card",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                NfcStage.PAYMENT_SUCCESS -> {
                    // 6. Payment Successful
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = VibrantTeal,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Payment Successful",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = VibrantTeal
                    )
                }

                else -> {
                    // Fallback
                    CircularProgressIndicator(color = VibrantTeal)
                }
            }
        }
    }
}

@Composable
fun NFCWaveAnimation(isActive: Boolean, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "nfc_wave")

    val waves = listOf(0, 500, 1000).map { delay ->
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, delayMillis = delay),
                repeatMode = RepeatMode.Restart
            ),
            label = "scale_$delay"
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, delayMillis = delay),
                repeatMode = RepeatMode.Restart
            ),
            label = "alpha_$delay"
        )
        scale to alpha
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            if (isActive) {
                waves.forEach { (scale, alpha) ->
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VibrantTeal.copy(alpha = alpha),
                                GradientMiddle.copy(alpha = alpha * 0.5f),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension * scale / 2
                    )
                }
            }
        }

        // Center NFC icon
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Contactless,
                    contentDescription = "NFC",
                    tint = VibrantTeal,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
