package com.example.floussi.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddPayment : Screen("add_payment")
    object PhonePayment : Screen("phone_payment")
    object QRCode : Screen("qr_code")
    object NFCPayment : Screen("nfc_payment")
    object Success : Screen("success")
    object Analytics : Screen("analytics")
    object Catalog : Screen("catalog") // Placeholder for future
}
