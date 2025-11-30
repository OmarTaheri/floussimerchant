package com.example.floussi.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shapes for specific components
val CardShape = RoundedCornerShape(20.dp)
val SheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
val ButtonShape = RoundedCornerShape(16.dp)
val InputShape = RoundedCornerShape(12.dp)
