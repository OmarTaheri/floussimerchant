package com.example.floussi.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.floussi.data.model.MerchantAccount
import com.example.floussi.ui.theme.SuccessGreen
import com.example.floussi.util.HapticFeedbackManager
import com.example.floussi.util.HapticType

@Composable
fun TopHeader(
    merchantAccount: MerchantAccount,
    hapticFeedback: HapticFeedbackManager,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Side - Avatar and Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar with Status Dot
            Box {
                AsyncImage(
                    model = merchantAccount.avatarUrl ?: "https://ui-avatars.com/api/?name=${merchantAccount.merchantName}&background=FFD166&color=0A1628",
                    contentDescription = "Profile Avatar",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable {
                            hapticFeedback.performHapticFeedback(HapticType.CLICK)
                        },
                    contentScale = ContentScale.Crop
                )

                // Status dot
                if (merchantAccount.isActive) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            // Account Type and Name
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = merchantAccount.accountType,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = merchantAccount.merchantName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Right Side - Settings Icon
        IconButton(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticType.CLICK)
                onSettingsClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
