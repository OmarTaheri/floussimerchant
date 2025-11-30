package com.example.floussi.ui.screens.home.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.floussi.data.model.Transaction
import com.example.floussi.data.model.TransactionType
import com.example.floussi.ui.theme.SuccessGreen
import com.example.floussi.ui.theme.ErrorRed
import com.example.floussi.ui.theme.GradientMiddle
import com.example.floussi.ui.theme.SheetShape
import com.example.floussi.util.toCurrency
import com.example.floussi.util.toRelativeTime

@Composable
fun TransactionSheet(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    
    // Collapsed height = Screen height - Top Content (~364dp)
    // Top Content = Header (80) + Spacer (24) + Card (220) + Spacer (40) = 364
    // Collapsed height = Screen height - Top Content (~280dp)
    // Top Content = Header (80) + Spacer (24) + Card (220) + Spacer (40) = 364 -> Reduced to give more space
    val collapsedHeight = screenHeight - 280.dp
    
    // Expanded height = Screen height - Top Header (~100dp)
    val expandedHeight = screenHeight - 100.dp
    
    val sheetHeight by animateDpAsState(
        targetValue = if (isExpanded) expandedHeight else collapsedHeight,
        label = "sheet_height"
    )

    var dragOffset by remember { mutableFloatStateOf(0f) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (dragOffset < -50) {
                            isExpanded = true
                        } else if (dragOffset > 50) {
                            isExpanded = false
                        }
                        dragOffset = 0f
                    }
                ) { _, dragAmount ->
                    dragOffset += dragAmount
                }
            },
        shape = SheetShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            // Drag Handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transactions List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp, 
                    end = 20.dp, 
                    top = 8.dp, 
                    bottom = 100.dp // Add padding for Bottom Nav
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (transaction.type) {
                        TransactionType.PAYMENT_RECEIVED -> GradientMiddle.copy(alpha = 0.15f)
                        TransactionType.REFUND -> ErrorRed.copy(alpha = 0.15f)
                        TransactionType.WITHDRAWAL -> ErrorRed.copy(alpha = 0.15f)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (transaction.type) {
                                TransactionType.PAYMENT_RECEIVED -> Icons.Default.ArrowDownward
                                TransactionType.REFUND -> Icons.Default.ArrowUpward
                                TransactionType.WITHDRAWAL -> Icons.Default.ArrowUpward
                            },
                            contentDescription = null,
                            tint = when (transaction.type) {
                                TransactionType.PAYMENT_RECEIVED -> GradientMiddle
                                TransactionType.REFUND -> ErrorRed
                                TransactionType.WITHDRAWAL -> ErrorRed
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Transaction Details
                Column {
                    Text(
                        text = transaction.customerName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = transaction.timestamp.toRelativeTime(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Amount
            Text(
                text = "${if (transaction.type == TransactionType.PAYMENT_RECEIVED) "+" else "-"}${transaction.amount.toCurrency(transaction.currency)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = when (transaction.type) {
                    TransactionType.PAYMENT_RECEIVED -> GradientMiddle
                    TransactionType.REFUND -> ErrorRed
                    TransactionType.WITHDRAWAL -> ErrorRed
                }
            )
        }
    }
}
