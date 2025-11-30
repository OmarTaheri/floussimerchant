package com.example.floussi.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*

@OptIn(ExperimentalAnimationApi::class)
object NavAnimations {

    // Circular reveal from FAB
    fun circularRevealEnter(): EnterTransition {
        return scaleIn(
            initialScale = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    fun circularRevealExit(): ExitTransition {
        return scaleOut(
            targetScale = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    // Slide up from bottom
    fun slideUpEnter(): EnterTransition {
        return slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn()
    }

    fun slideDownExit(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeOut()
    }

    // Fade transition
    fun fadeEnter(): EnterTransition {
        return fadeIn(animationSpec = tween(500))
    }

    fun fadeExit(): ExitTransition {
        return fadeOut(animationSpec = tween(500))
    }
}
