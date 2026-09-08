package com.helpsetu.app.data.model

data class Worker(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val category: String = "", // ELECTRICIAN, PLUMBER, LABOURER, CARPENTER
    val categoryHindi: String = "",
    val rating: Double = 4.8,
    val jobsCompleted: Int = 24,
    val distanceKm: Double = 1.2,
    val experienceYears: Int = 5,
    val hourlyRate: String = "₹250/hr",
    val workDetails: String = "",
    val location: String = "New Delhi",
    val isVerified: Boolean = true,
    val isAvailable: Boolean = true,
    val isUserCreated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    // No-arg constructor for Firestore deserialization
    constructor() : this(
        id = "",
        name = "",
        phone = "",
        category = "",
        categoryHindi = "",
        rating = 4.8,
        jobsCompleted = 0,
        distanceKm = 1.0,
        experienceYears = 1,
        hourlyRate = "",
        workDetails = "",
        location = "",
        isVerified = true,
        isAvailable = true,
        isUserCreated = false,
        createdAt = System.currentTimeMillis()
    )

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "phone" to phone,
            "category" to category,
            "categoryHindi" to categoryHindi,
            "rating" to rating,
            "jobsCompleted" to jobsCompleted,
            "distanceKm" to distanceKm,
            "experienceYears" to experienceYears,
            "hourlyRate" to hourlyRate,
            "workDetails" to workDetails,
            "location" to location,
            "isVerified" to isVerified,
            "isAvailable" to isAvailable,
            "isUserCreated" to isUserCreated,
            "createdAt" to createdAt
        )
    }
}

enum class ServiceCategory(
    val id: String,
    val titleEnglish: String,
    val titleHindi: String,
    val description: String
) {
    ELECTRICIAN(
        id = "ELECTRICIAN",
        titleEnglish = "Electrician",
        titleHindi = "बिजली वाला",
        description = "Wiring, switches, fan & appliance repairs"
    ),
    PLUMBER(
        id = "PLUMBER",
        titleEnglish = "Plumber",
        titleHindi = "नल ठीक करने वाला",
        description = "Pipe leaks, tap fixing, bathroom fittings"
    ),
    LABOURER(
        id = "LABOURER",
        titleEnglish = "Labourer",
        titleHindi = "दिहाड़ी मजदूर",
        description = "Loading, shifting, construction & helper"
    ),
    CARPENTER(
        id = "CARPENTER",
        titleEnglish = "Carpenter",
        titleHindi = "बढ़ई",
        description = "Furniture repair, doors, wood polish & fittings"
    );

    companion object {
        fun fromId(id: String?): ServiceCategory? {
            return values().firstOrNull { it.id.equals(id, ignoreCase = true) }
        }
    }
}
