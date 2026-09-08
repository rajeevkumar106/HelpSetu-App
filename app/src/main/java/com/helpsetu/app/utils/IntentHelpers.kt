package com.helpsetu.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object IntentHelpers {

    fun makePhoneCall(context: Context, phoneNumber: String) {
        try {
            val cleanNumber = phoneNumber.replace(" ", "").replace("-", "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open dialer: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(
        context: Context,
        phoneNumber: String,
        message: String = "Hello, I found your profile on HelpSetu app. Are you available for work?"
    ) {
        try {
            // Clean phone number: remove non-digits except +
            var cleanPhone = phoneNumber.replace("[^0-9+]".toRegex(), "")
            if (cleanPhone.startsWith("+")) {
                cleanPhone = cleanPhone.substring(1)
            } else if (cleanPhone.length == 10) {
                // Default to India country code 91 if 10 digits
                cleanPhone = "91$cleanPhone"
            }

            val encodedMessage = Uri.encode(message)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMessage")

            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web browser or show toast
            try {
                var cleanPhone = phoneNumber.replace("[^0-9+]".toRegex(), "")
                if (cleanPhone.startsWith("+")) cleanPhone = cleanPhone.substring(1)
                val fallbackUri = Uri.parse("https://wa.me/$cleanPhone")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (fallbackEx: Exception) {
                Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
