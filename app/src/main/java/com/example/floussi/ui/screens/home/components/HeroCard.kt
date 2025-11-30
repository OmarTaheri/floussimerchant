package com.example.floussi.ui.screens.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floussi.data.model.MerchantAccount
import com.example.floussi.ui.theme.*
import com.example.floussi.util.*

@Composable
fun HeroCard(
    merchantAccount: MerchantAccount,
    hapticFeedback: HapticFeedbackManager,
    onQRCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gyroscope effect for card tilt
    val gyroscopeData by rememberGyroscopeState()

    // Calculate tilt based on gyroscope
    val rotationX by animateFloatAsState(
        targetValue = gyroscopeData.y * 5f, // Multiply for more pronounced effect
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "rotation_x"
    )

    val rotationY by animateFloatAsState(
        targetValue = -gyroscopeData.x * 5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "rotation_y"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = DeepMidnightBlue.copy(alpha = 0.3f),
                spotColor = VibrantTeal.copy(alpha = 0.2f)
            )
            .graphicsLayer {
                this.rotationX = rotationX.coerceIn(-15f, 15f)
                this.rotationY = rotationY.coerceIn(-15f, 15f)
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GradientStart,
                            GradientMiddle,
                            GradientEnd,
                            GradientStart.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row - Card branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = com.example.floussi.R.drawable.flossi),
                        contentDescription = "Floussi Logo",
                        modifier = Modifier
                            .height(26.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    // Card type indicator
                    Text(
                        text = "MERCHANT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp
                        ),
                        color = TextOnDark.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Center - Balance Section (Main focus)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CURRENT BALANCE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp
                        ),
                        color = TextOnDark.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${merchantAccount.balance.toBalanceFormat()} MAD",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        ),
                        color = TextOnDark
                    )
                }

                // Bottom - User ID (Card Number)
                Column {
                    Text(
                        text = "CARD ID",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp
                        ),
                        color = TextOnDark.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = merchantAccount.cardId,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 2.sp
                        ),
                        color = TextOnDark.copy(alpha = 0.9f)
                    )
                }
            }

            // Subtle accent glow in corner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VibrantTeal.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            center = androidx.compose.ui.geometry.Offset(0f, 0f),
                            radius = 400f
                        )
                    )
            )
        }
    }
}

