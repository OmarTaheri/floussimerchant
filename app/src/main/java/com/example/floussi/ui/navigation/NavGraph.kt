package com.example.floussi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.floussi.ui.screens.analytics.AnalyticsScreen
import com.example.floussi.ui.screens.home.HomeScreen
import com.example.floussi.ui.screens.home.HomeViewModel
import com.example.floussi.ui.screens.nfc.NFCPaymentScreen
import com.example.floussi.ui.screens.nfc.NFCPaymentViewModel
import com.example.floussi.ui.screens.payment.AddPaymentScreen
import com.example.floussi.ui.screens.payment.AddPaymentViewModel
import com.example.floussi.ui.screens.phonepayment.PhonePaymentScreen
import com.example.floussi.ui.screens.phonepayment.PhonePaymentViewModel
import com.example.floussi.ui.screens.qr.QRCodeScreen
import com.example.floussi.ui.screens.qr.QRCodeViewModel
import com.example.floussi.ui.screens.splash.SplashScreen
import com.example.floussi.ui.screens.success.SuccessScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(
            route = Screen.Splash.route,
            enterTransition = { NavAnimations.fadeEnter() },
            exitTransition = { NavAnimations.fadeExit() }
        ) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(
            route = Screen.Home.route,
            enterTransition = { NavAnimations.fadeEnter() },
            exitTransition = { NavAnimations.fadeExit() }
        ) {
            val viewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = viewModel,
                onAddPaymentClick = {
                    navController.navigate(Screen.AddPayment.route)
                },
                onPhonePaymentClick = {
                    navController.navigate(Screen.PhonePayment.route)
                },
                onQRCodeClick = {
                    navController.navigate(Screen.QRCode.route)
                },
                onNFCPaymentClick = {
                    navController.navigate(Screen.NFCPayment.route)
                },
                onAnalyticsClick = {
                    navController.navigate(Screen.Analytics.route)
                }
            )
        }

        // Add Payment Screen
        composable(
            route = Screen.AddPayment.route,
            enterTransition = { NavAnimations.circularRevealEnter() },
            exitTransition = { NavAnimations.circularRevealExit() }
        ) {
            val viewModel: AddPaymentViewModel = viewModel()
            AddPaymentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.Success.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // QR Code Screen
        composable(
            route = Screen.QRCode.route,
            enterTransition = { NavAnimations.slideUpEnter() },
            exitTransition = { NavAnimations.slideDownExit() }
        ) {
            val viewModel: QRCodeViewModel = viewModel()
            QRCodeScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Phone Payment Screen
        composable(
            route = Screen.PhonePayment.route,
            enterTransition = { NavAnimations.slideUpEnter() },
            exitTransition = { NavAnimations.slideDownExit() }
        ) {
            val viewModel: PhonePaymentViewModel = viewModel()
            PhonePaymentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.Success.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // NFC Payment Screen
        composable(
            route = Screen.NFCPayment.route,
            enterTransition = { NavAnimations.fadeEnter() },
            exitTransition = { NavAnimations.fadeExit() }
        ) {
            val viewModel: NFCPaymentViewModel = viewModel()
            NFCPaymentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.Success.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // Analytics Screen
        composable(
            route = Screen.Analytics.route,
            enterTransition = { NavAnimations.fadeEnter() },
            exitTransition = { NavAnimations.fadeExit() }
        ) {
            AnalyticsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Success Screen
        composable(
            route = Screen.Success.route,
            enterTransition = { NavAnimations.fadeEnter() },
            exitTransition = { NavAnimations.fadeExit() }
        ) {
            SuccessScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
