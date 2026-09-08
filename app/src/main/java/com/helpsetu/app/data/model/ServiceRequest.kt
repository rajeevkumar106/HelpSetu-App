package com.helpsetu.app.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ServiceRequest(
    val id: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val workerPhone: String = "",
    val workerCategory: String = "",
    val customerPhone: String = "Self",
    val contactType: String = "CALL", // "CALL" or "WHATSAPP"
    val statusTag: String = "Contacted", // "Contacted" or "WhatsApped"
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
) {
    // No-arg constructor for Firestore
    constructor() : this(
        id = "",
        workerId = "",
        workerName = "",
        workerPhone = "",
        workerCategory = "",
        customerPhone = "Self",
        contactType = "CALL",
        statusTag = "Contacted",
        timestamp = System.currentTimeMillis(),
        notes = ""
    )

    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "workerId" to workerId,
            "workerName" to workerName,
            "workerPhone" to workerPhone,
            "workerCategory" to workerCategory,
            "customerPhone" to customerPhone,
            "contactType" to contactType,
            "statusTag" to statusTag,
            "timestamp" to timestamp,
            "notes" to notes
        )
    }
}
