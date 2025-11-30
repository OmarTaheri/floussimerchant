package com.example.floussi.util

import android.app.Activity
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import java.nio.charset.Charset

data class NfcCard(
    val uid: String,
    val cardType: String,
    val ndefData: String? = null
)

class NFCManager(private val context: Context) {
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    fun isNFCAvailable(): Boolean = nfcAdapter != null

    fun isNFCEnabled(): Boolean = nfcAdapter?.isEnabled ?: false

    fun enableReaderMode(
        activity: Activity,
        callback: (NfcCard) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val adapter = nfcAdapter
        if (adapter == null) {
            onError("NFC not supported on this device")
            return
        }

        val flags = NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

        val extras = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 100)
        }

        try {
            adapter.enableReaderMode(
                activity,
                { tag ->
                    try {
                        val card = parseNfcTag(tag)
                        activity.runOnUiThread { callback(card) }
                    } catch (e: Exception) {
                        Log.e("NFCManager", "Error parsing NFC tag", e)
                        activity.runOnUiThread { onError("NFC read error") }
                    }
                },
                flags,
                extras
            )
        } catch (e: Exception) {
            Log.e("NFCManager", "Failed to enable reader mode", e)
            onError("Unable to start NFC reader")
        }
    }

    fun disableReaderMode(activity: Activity) {
        try {
            nfcAdapter?.disableReaderMode(activity)
        } catch (e: Exception) {
            Log.w("NFCManager", "Failed to disable reader mode", e)
        }
    }

    private fun parseNfcTag(tag: Tag): NfcCard {
        val uid = tag.id?.joinToString(separator = "") { byte -> "%02X".format(byte) } ?: "Unknown"
        val techList = tag.techList.joinToString(",") { it.substringAfterLast('.') }.ifEmpty { "Unknown" }

        val ndefData = Ndef.get(tag)?.cachedNdefMessage?.toReadableString()
        val cardType = when {
            techList.contains("IsoDep", ignoreCase = true) -> "ISO-DEP"
            techList.contains("NfcA", ignoreCase = true) -> "NFC-A"
            techList.contains("NfcB", ignoreCase = true) -> "NFC-B"
            techList.contains("NfcF", ignoreCase = true) -> "NFC-F"
            techList.contains("NfcV", ignoreCase = true) -> "NFC-V"
            else -> techList
        }

        return NfcCard(
            uid = uid,
            cardType = cardType,
            ndefData = ndefData
        )
    }
}

private fun NdefMessage.toReadableString(): String? {
    val payload = records.joinToString(" | ") { record ->
        val type = runCatching { String(record.type, Charset.defaultCharset()) }.getOrDefault("NDEF")
        val data = runCatching { String(record.payload, Charset.forName("UTF-8")).trim('\u0000') }
            .getOrDefault("")
        "$type: $data"
    }
    return payload.ifBlank { null }
}
