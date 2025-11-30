package com.example.floussi.ui.screens.payment.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floussi.util.HapticFeedbackManager
import com.example.floussi.util.HapticType

@Composable
fun NumericKeypad(
    hapticFeedback: HapticFeedbackManager,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: 1, 2, 3
        KeypadRow(
            digits = listOf("1", "2", "3"),
            hapticFeedback = hapticFeedback,
            onDigitClick = onDigitClick
        )

        // Row 2: 4, 5, 6
        KeypadRow(
            digits = listOf("4", "5", "6"),
            hapticFeedback = hapticFeedback,
            onDigitClick = onDigitClick
        )

        // Row 3: 7, 8, 9
        KeypadRow(
            digits = listOf("7", "8", "9"),
            hapticFeedback = hapticFeedback,
            onDigitClick = onDigitClick
        )

        // Row 4: ., 0, Delete
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeypadButton(
                text = ".",
                hapticFeedback = hapticFeedback,
                onClick = { onDigitClick(".") },
                modifier = Modifier.weight(1f)
            )

            KeypadButton(
                text = "0",
                hapticFeedback = hapticFeedback,
                onClick = { onDigitClick("0") },
                modifier = Modifier.weight(1f)
            )

            KeypadIconButton(
                hapticFeedback = hapticFeedback,
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeypadRow(
    digits: List<String>,
    hapticFeedback: HapticFeedbackManager,
    onDigitClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        digits.forEach { digit ->
            KeypadButton(
                text = digit,
                hapticFeedback = hapticFeedback,
                onClick = { onDigitClick(digit) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    hapticFeedback: HapticFeedbackManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )

    Surface(
        modifier = modifier
            .height(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                isPressed = true
                hapticFeedback.performHapticFeedback(HapticType.CLICK)
                onClick()
            },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun KeypadIconButton(
    hapticFeedback: HapticFeedbackManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "icon_button_scale"
    )

    Surface(
        modifier = modifier
            .height(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                isPressed = true
                hapticFeedback.performHapticFeedback(HapticType.CLICK)
                onClick()
            },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}
