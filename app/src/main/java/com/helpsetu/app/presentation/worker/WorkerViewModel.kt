package com.helpsetu.app.presentation.worker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpsetu.app.data.model.ServiceCategory
import com.helpsetu.app.data.model.ServiceRequest
import com.helpsetu.app.data.model.Worker
import com.helpsetu.app.data.repository.WorkerRepository
import com.helpsetu.app.utils.IntentHelpers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppNotification(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val iconType: String // "WORKER", "VERIFIED", "INFO", "ALERT"
)

class WorkerViewModel(
    private val repository: WorkerRepository = WorkerRepository.getInstance()
) : ViewModel() {

    private val _currentLocation = MutableStateFlow("Noida, Sector 62 (Delhi NCR)")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("HI") // "HI" (Hindi) or "EN" (English)
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _unreadNotificationsCount = MutableStateFlow(2)
    val unreadNotificationsCount: StateFlow<Int> = _unreadNotificationsCount.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            AppNotification(
                id = "n1",
                title = "Active Local Workers",
                description = "4 electricians and 2 plumbers are active near your location in Noida Sector 62.",
                timeAgo = "5m ago",
                iconType = "WORKER"
            ),
            AppNotification(
                id = "n2",
                title = "Aadhaar KYC Verified",
                description = "100% of workers in your area have verified Aadhaar identification.",
                timeAgo = "1h ago",
                iconType = "VERIFIED"
            ),
            AppNotification(
                id = "n3",
                title = "Direct Connect Enabled",
                description = "Zero middleman fee. Call or WhatsApp workers directly with transparent pricing.",
                timeAgo = "1d ago",
                iconType = "INFO"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _userRole = MutableStateFlow("CUSTOMER") // "CUSTOMER" or "WORKER"
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    private val _currentUserPhone = MutableStateFlow("+919876501234")
    val currentUserPhone: StateFlow<String> = _currentUserPhone.asStateFlow()

    val workers: StateFlow<List<Worker>> = repository.workers
    val serviceRequests: StateFlow<List<ServiceRequest>> = repository.serviceRequests

    // Only workers created by this user as a worker (for Profile Section)
    val myWorkerProfiles: StateFlow<List<Worker>> = combine(
        repository.workers,
        repository.userCreatedWorkerIds
    ) { allWorkers, createdIds ->
        allWorkers.filter { it.isUserCreated || it.id in createdIds }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val _selectedProfileId = MutableStateFlow<String?>(null)
    val selectedProfileId: StateFlow<String?> = _selectedProfileId.asStateFlow()

    val currentProfile: StateFlow<Worker?> = combine(
        myWorkerProfiles,
        _selectedProfileId
    ) { myList, id ->
        if (id == null) {
            myList.firstOrNull()
        } else {
            myList.find { it.id == id } ?: myList.firstOrNull()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    // Reactive filtered workers based on selected category and search query
    val filteredWorkers: StateFlow<List<Worker>> = combine(
        workers,
        _selectedCategory,
        _searchQuery
    ) { workerList, category, query ->
        var list = workerList
        if (!category.isNullOrBlank()) {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { worker ->
                worker.name.lowercase().contains(q) ||
                        worker.category.lowercase().contains(q) ||
                        worker.categoryHindi.lowercase().contains(q) ||
                        worker.workDetails.lowercase().contains(q) ||
                        worker.location.lowercase().contains(q)
            }
        }
        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.workers.value
    )

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setUserRole(role: String) {
        _userRole.value = role
    }

    fun setCustomerPhone(phone: String) {
        _currentUserPhone.value = phone
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    fun updateLocation(location: String) {
        _currentLocation.value = location
        _actionMessage.value = "Location updated to $location"
    }

    fun toggleLanguage() {
        val next = if (_selectedLanguage.value == "HI") "EN" else "HI"
        _selectedLanguage.value = next
        _actionMessage.value = if (next == "HI") "भाषा हिंदी में बदली गई (Hindi Selected)" else "Language switched to English"
    }

    fun clearNotifications() {
        _unreadNotificationsCount.value = 0
    }

    fun callHelpline(context: Context) {
        IntentHelpers.makePhoneCall(context, "18002004357")
        _actionMessage.value = "Connecting to 24x7 HelpSetu Support Helpline (1800-200-HELP)"
    }

    fun contactWorker(context: Context, worker: Worker, contactType: String) {
        viewModelScope.launch {
            // Log contact event to Firestore and local state
            repository.logContact(worker, contactType, _currentUserPhone.value)

            val typeText = if (contactType.equals("WHATSAPP", ignoreCase = true)) "WhatsApp inquiry" else "Call"
            _actionMessage.value = "Logged $typeText for ${worker.name} in My Requests"

            // Trigger real intent
            if (contactType.equals("WHATSAPP", ignoreCase = true)) {
                val message = "Namaste ${worker.name} ji, I found your ${worker.category.lowercase()} profile on HelpSetu. I need help at my location. Are you available?"
                IntentHelpers.openWhatsApp(context, worker.phone, message)
            } else {
                IntentHelpers.makePhoneCall(context, worker.phone)
            }
        }
    }

    fun selectProfile(profileId: String?) {
        _selectedProfileId.value = profileId
    }

    fun createProfile(
        name: String,
        phone: String,
        category: String,
        workDetails: String,
        location: String,
        hourlyRate: String,
        experienceYears: Int,
        isAvailable: Boolean = true
    ): Boolean {
        if (name.isBlank() || phone.isBlank() || category.isBlank()) {
            _actionMessage.value = "Please fill in all mandatory fields (Name, Phone, Category)"
            return false
        }

        val categoryObj = ServiceCategory.fromId(category)
        val hindiTitle = categoryObj?.titleHindi ?: category

        val newId = java.util.UUID.randomUUID().toString()
        val newWorker = Worker(
            id = newId,
            name = name.trim(),
            phone = phone.trim(),
            category = category.trim().uppercase(),
            categoryHindi = hindiTitle,
            rating = 5.0,
            jobsCompleted = 1,
            distanceKm = 0.5,
            experienceYears = experienceYears.coerceAtLeast(1),
            hourlyRate = if (hourlyRate.isNotBlank()) hourlyRate.trim() else "₹250/hr",
            workDetails = workDetails.trim(),
            location = if (location.isNotBlank()) location.trim() else "Local Area",
            isVerified = true,
            isAvailable = isAvailable,
            isUserCreated = true,
            createdAt = System.currentTimeMillis()
        )

        repository.saveWorker(newWorker)
        _selectedProfileId.value = newWorker.id
        _actionMessage.value = "Profile registered successfully! You are now visible to local customers."
        return true
    }

    fun updateProfile(
        id: String,
        name: String,
        phone: String,
        category: String,
        workDetails: String,
        location: String,
        hourlyRate: String,
        experienceYears: Int,
        isAvailable: Boolean
    ): Boolean {
        if (name.isBlank() || phone.isBlank() || category.isBlank()) {
            _actionMessage.value = "Please fill in Name, Phone, and Category"
            return false
        }

        val existing = repository.getWorkerById(id)
        val categoryObj = ServiceCategory.fromId(category)
        val hindiTitle = categoryObj?.titleHindi ?: (existing?.categoryHindi ?: category)

        val updated = Worker(
            id = id,
            name = name.trim(),
            phone = phone.trim(),
            category = category.trim().uppercase(),
            categoryHindi = hindiTitle,
            rating = existing?.rating ?: 5.0,
            jobsCompleted = existing?.jobsCompleted ?: 1,
            distanceKm = existing?.distanceKm ?: 0.5,
            experienceYears = experienceYears.coerceAtLeast(1),
            hourlyRate = if (hourlyRate.isNotBlank()) hourlyRate.trim() else (existing?.hourlyRate ?: "₹250/hr"),
            workDetails = workDetails.trim(),
            location = if (location.isNotBlank()) location.trim() else (existing?.location ?: "Local Area"),
            isVerified = existing?.isVerified ?: true,
            isAvailable = isAvailable,
            isUserCreated = existing?.isUserCreated ?: true,
            createdAt = existing?.createdAt ?: System.currentTimeMillis()
        )

        repository.saveWorker(updated)
        _actionMessage.value = "Profile updated successfully!"
        return true
    }

    fun deleteProfile(id: String): Boolean {
        repository.deleteWorker(id)
        val remaining = myWorkerProfiles.value.filter { it.id != id }
        _selectedProfileId.value = remaining.firstOrNull()?.id
        _actionMessage.value = "Profile deleted successfully."
        return true
    }

    fun toggleProfileAvailability(id: String) {
        val existing = repository.getWorkerById(id) ?: return
        val updated = existing.copy(isAvailable = !existing.isAvailable)
        repository.saveWorker(updated)
        _actionMessage.value = if (updated.isAvailable) "Status: Available & Online" else "Status: Busy / Offline"
    }

    fun registerWorker(
        name: String,
        phone: String,
        category: String,
        workDetails: String,
        location: String,
        hourlyRate: String,
        experienceYears: Int
    ): Boolean {
        return createProfile(
            name = name,
            phone = phone,
            category = category,
            workDetails = workDetails,
            location = location,
            hourlyRate = hourlyRate,
            experienceYears = experienceYears,
            isAvailable = true
        )
    }
}
