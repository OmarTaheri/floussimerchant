package com.example.floussi.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Simple fade-in animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logoFadeIn"
    )

    LaunchedEffect(Unit) {
        delay(1500)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF134671)),
        contentAlignment = Alignment.Center
    ) {
        // Clean, single logo with fade-in
        Image(
            painter = painterResource(id = com.example.floussi.R.drawable.f_w),
            contentDescription = "Floussi Logo",
            modifier = Modifier
                .size(180.dp)
                .alpha(alpha)
        )
    }
}
