package com.example.floussi.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.floussi.ui.screens.home.components.*
import com.example.floussi.util.rememberHapticFeedback
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddPaymentClick: () -> Unit,
    onPhonePaymentClick: () -> Unit,
    onQRCodeClick: () -> Unit,
    onNFCPaymentClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hapticFeedback = rememberHapticFeedback()
    val scrollState = rememberScrollState()

    // Staggered entry animations
    var showHeader by remember { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }

    val headerOffset by animateDpAsState(
        targetValue = if (showHeader) 0.dp else (-50).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "header_offset"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (showCard) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    val sheetOffset by animateDpAsState(
        targetValue = if (showSheet) 0.dp else 400.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sheet_offset"
    )

    // Parallax effect for card based on scroll
    val cardParallaxOffset = scrollState.value * 0.3f

    LaunchedEffect(Unit) {
        showHeader = true
        delay(100)
        showCard = true
        delay(200)
        showSheet = true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
        ) {
            // Top Header
            TopHeader(
                merchantAccount = uiState.merchantAccount,
                hapticFeedback = hapticFeedback,
                onSettingsClick = { /* TODO */ },
                modifier = Modifier.graphicsLayer {
                    translationY = headerOffset.toPx()
                    alpha = if (showHeader) 1f else 0f
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Hero Card
            HeroCard(
                merchantAccount = uiState.merchantAccount,
                hapticFeedback = hapticFeedback,
                onQRCodeClick = onQRCodeClick,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = cardScale
                        scaleY = cardScale
                        alpha = cardScale
                        translationY = -cardParallaxOffset
                    }
            )

            Spacer(modifier = Modifier.height(40.dp))

        }

        // Transaction Sheet
        TransactionSheet(
            transactions = uiState.transactions,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = sheetOffset.toPx()
                    alpha = if (showSheet) 1f else 0f
                }
        )

        // Bottom Navigation
        BottomNavigation(
            hapticFeedback = hapticFeedback,
            onAddClick = onAddPaymentClick,
            onPhonePaymentClick = onPhonePaymentClick,
            onQRCodeClick = onQRCodeClick,
            onNFCPaymentClick = onNFCPaymentClick,
            onAnalyticsClick = onAnalyticsClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
