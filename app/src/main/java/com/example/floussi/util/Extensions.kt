package com.example.floussi.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Number formatting
fun Double.toCurrency(currencyCode: String = "MAD"): String {
    val format = NumberFormat.getCurrencyInstance(Locale("ar", "MA"))
    format.currency = Currency.getInstance(currencyCode)
    return format.format(this)
}

fun Double.toBalanceFormat(): String {
    val format = NumberFormat.getNumberInstance(Locale.US)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(this)
}

// Date formatting
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} min ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> "${diff / 86400000}d ago"
    }
}

// Gradient modifier
fun Modifier.gradientBackground(colors: List<Color>): Modifier = this.drawBehind {
    drawRect(
        brush = Brush.linearGradient(colors)
    )
}

// Share bitmap
fun Context.shareBitmap(bitmap: Bitmap, fileName: String = "qr_code.png") {
    try {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val file = File(cachePath, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(contentUri, contentResolver.getType(contentUri))
            putExtra(Intent.EXTRA_STREAM, contentUri)
        }

        startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Save bitmap to downloads
fun Context.saveBitmapToDownloads(bitmap: Bitmap, fileName: String = "qr_code.png"): Boolean {
    return try {
        val downloadsDir = getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
