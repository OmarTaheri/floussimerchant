package com.example.floussi.ui.screens.home.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.floussi.ui.theme.*
import com.example.floussi.util.HapticFeedbackManager
import com.example.floussi.util.HapticType
import kotlinx.coroutines.delay

@Composable
fun BottomNavigation(
    hapticFeedback: HapticFeedbackManager,
    onAddClick: () -> Unit,
    onPhonePaymentClick: () -> Unit,
    onQRCodeClick: () -> Unit,
    onNFCPaymentClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(-1) }
    var fabPressed by remember { mutableStateOf(false) }

    val fabScale by animateDpAsState(
        targetValue = if (fabPressed) 60.dp else 64.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "fab_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        // Bottom Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
        // Left tabs
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SMS Payment Tab
            NavItem(
                icon = Icons.Default.Sms,
                label = "SMS",
                isSelected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
                    hapticFeedback.performHapticFeedback(HapticType.CLICK)
                    onPhonePaymentClick()
                        }
                    )

                    // QR Code Tab
                    NavItem(
                        icon = Icons.Default.QrCode2,
                        label = "QR Code",
                        isSelected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            hapticFeedback.performHapticFeedback(HapticType.CLICK)
                            onQRCodeClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.width(80.dp)) // Space for FAB

                // Right tabs
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // NFC Payment Tab
                    NavItem(
                        icon = Icons.Default.Contactless,
                        label = "NFC",
                        isSelected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            hapticFeedback.performHapticFeedback(HapticType.CLICK)
                            onNFCPaymentClick()
                        }
                    )

                    // Analytics Tab (Locked)
                    NavItem(
                        icon = Icons.Default.Analytics,
                        label = "Analytics",
                        isSelected = selectedTab == 3,
                        isLocked = true,
                        onClick = {
                            selectedTab = 3
                            hapticFeedback.performHapticFeedback(HapticType.CLICK)
                            onAnalyticsClick()
                        }
                    )
                }
            }
        }

        // Floating Action Button (Center) with gradient
        Box(
            modifier = Modifier
                .size(fabScale)
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp)
                .shadow(
                    elevation = if (fabPressed) 12.dp else 6.dp,
                    shape = CircleShape,
                    ambientColor = DeepMidnightBlue.copy(alpha = 0.3f),
                    spotColor = VibrantTeal.copy(alpha = 0.2f)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GradientStart,
                            GradientMiddle,
                            GradientEnd
                        )
                    ),
                    shape = CircleShape
                )
                .clickable {
                    fabPressed = true
                    hapticFeedback.performHapticFeedback(HapticType.HEAVY_CLICK)
                    onAddClick()
                },
            contentAlignment = Alignment.Center
        ) {
            // Subtle radial glow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VibrantTeal.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Payment",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    LaunchedEffect(fabPressed) {
        if (fabPressed) {
            delay(120)
            fabPressed = false
        }
    }
}

@Composable
fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isLocked: Boolean = false
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                VibrantTeal.copy(alpha = 0.15f),
                GradientMiddle.copy(alpha = 0.1f)
            )
        )
    } else {
        Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent))
    }

    Box(
        modifier = Modifier
            .background(
                brush = backgroundBrush,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) VibrantTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                // Lock icon overlay for locked items
                if (isLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.TopEnd),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) VibrantTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
