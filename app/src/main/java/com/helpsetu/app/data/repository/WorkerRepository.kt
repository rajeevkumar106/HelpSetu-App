package com.helpsetu.app.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.helpsetu.app.data.model.ServiceCategory
import com.helpsetu.app.data.model.ServiceRequest
import com.helpsetu.app.data.model.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class WorkerRepository private constructor() {

    private val TAG = "WorkerRepository"
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _workers = MutableStateFlow<List<Worker>>(emptyList())
    val workers: StateFlow<List<Worker>> = _workers.asStateFlow()

    private val _userCreatedWorkerIds = MutableStateFlow<Set<String>>(emptySet())
    val userCreatedWorkerIds: StateFlow<Set<String>> = _userCreatedWorkerIds.asStateFlow()

    private val _serviceRequests = MutableStateFlow<List<ServiceRequest>>(emptyList())
    val serviceRequests: StateFlow<List<ServiceRequest>> = _serviceRequests.asStateFlow()

    private var firestore: FirebaseFirestore? = null

    init {
        // 1. Preload realistic initial local data
        _workers.value = getInitialWorkers()
        _serviceRequests.value = getInitialRequests()

        // 2. Initialize Firestore safely if Firebase is configured
        initFirestore()
    }

    private fun initFirestore() {
        try {
            if (FirebaseApp.getApps(FirebaseApp.getInstance().applicationContext).isNotEmpty()) {
                firestore = FirebaseFirestore.getInstance()
                listenToFirestoreWorkers()
                listenToFirestoreRequests()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firestore initialization skipped: ${e.message}. Using reactive local store.")
        }
    }

    private fun listenToFirestoreWorkers() {
        val db = firestore ?: return
        try {
            db.collection("workers")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Worker snapshot listen error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val firestoreList = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Worker::class.java)?.copy(id = doc.id)
                        }
                        // Merge with initial local list avoiding duplicates
                        val merged = (firestoreList + _workers.value).distinctBy { it.id }
                        _workers.value = merged
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed listening to workers: ${e.message}")
        }
    }

    private fun listenToFirestoreRequests() {
        val db = firestore ?: return
        try {
            db.collection("service_requests")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Requests snapshot listen error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val firestoreList = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(ServiceRequest::class.java)?.copy(id = doc.id)
                        }
                        val merged = (firestoreList + _serviceRequests.value).distinctBy { it.id }
                        _serviceRequests.value = merged.sortedByDescending { it.timestamp }
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed listening to requests: ${e.message}")
        }
    }

    fun logContact(worker: Worker, contactType: String, customerPhone: String = "Self") {
        val newRequest = ServiceRequest(
            id = UUID.randomUUID().toString(),
            workerId = worker.id,
            workerName = worker.name,
            workerPhone = worker.phone,
            workerCategory = worker.category,
            customerPhone = customerPhone,
            contactType = contactType.uppercase(),
            statusTag = if (contactType.equals("WHATSAPP", ignoreCase = true)) "WhatsApped" else "Contacted",
            timestamp = System.currentTimeMillis(),
            notes = "Inquiry for ${worker.category.lowercase()} service"
        )

        // 1. Immediately update reactive state so UI reflects instant feedback
        _serviceRequests.value = listOf(newRequest) + _serviceRequests.value

        // 2. Persist to Firestore asynchronously
        scope.launch {
            try {
                firestore?.collection("service_requests")
                    ?.document(newRequest.id)
                    ?.set(newRequest.toMap())
                    ?.addOnSuccessListener {
                        Log.d(TAG, "Contact logged in Firestore: ${newRequest.id}")
                    }
                    ?.addOnFailureListener { e ->
                        Log.w(TAG, "Firestore write error: ${e.message}")
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to log contact to Firestore: ${e.message}")
            }
        }
    }

    fun saveWorker(worker: Worker) {
        val targetWorker = if (worker.id.isBlank()) {
            worker.copy(id = UUID.randomUUID().toString())
        } else {
            worker
        }

        if (targetWorker.isUserCreated) {
            _userCreatedWorkerIds.value = _userCreatedWorkerIds.value + targetWorker.id
        }

        // 1. Update local reactive state
        val current = _workers.value.toMutableList()
        val index = current.indexOfFirst { it.id == targetWorker.id }
        if (index >= 0) {
            current[index] = targetWorker
        } else {
            current.add(0, targetWorker)
        }
        _workers.value = current

        // 2. Persist to Firestore
        scope.launch {
            try {
                firestore?.collection("workers")
                    ?.document(targetWorker.id)
                    ?.set(targetWorker.toMap())
                    ?.addOnSuccessListener {
                        Log.d(TAG, "Worker saved in Firestore: ${targetWorker.id}")
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to save worker to Firestore: ${e.message}")
            }
        }
    }

    fun deleteWorker(workerId: String) {
        // 1. Update local reactive state
        _userCreatedWorkerIds.value = _userCreatedWorkerIds.value - workerId
        _workers.value = _workers.value.filter { it.id != workerId }

        // 2. Delete from Firestore
        scope.launch {
            try {
                firestore?.collection("workers")
                    ?.document(workerId)
                    ?.delete()
                    ?.addOnSuccessListener {
                        Log.d(TAG, "Worker deleted from Firestore: $workerId")
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to delete worker from Firestore: ${e.message}")
            }
        }
    }

    fun getWorkerById(workerId: String): Worker? {
        return _workers.value.find { it.id == workerId }
    }

    private fun getInitialWorkers(): List<Worker> {
        return listOf(
            // Electricians
            Worker(
                id = "w1",
                name = "Ramesh Kumar Sharma",
                phone = "+919876543210",
                category = ServiceCategory.ELECTRICIAN.id,
                categoryHindi = ServiceCategory.ELECTRICIAN.titleHindi,
                rating = 4.9,
                jobsCompleted = 42,
                distanceKm = 0.8,
                experienceYears = 8,
                hourlyRate = "₹250/hr",
                workDetails = "Complete home wiring, MCB tripping, fan, geyser & inverter repairs.",
                location = "Sector 14, Main Market",
                isVerified = true,
                isAvailable = true
            ),
            Worker(
                id = "w2",
                name = "Mohammad Aslam",
                phone = "+919811223344",
                category = ServiceCategory.ELECTRICIAN.id,
                categoryHindi = ServiceCategory.ELECTRICIAN.titleHindi,
                rating = 4.7,
                jobsCompleted = 28,
                distanceKm = 1.6,
                experienceYears = 5,
                hourlyRate = "₹200/hr",
                workDetails = "LED lighting, switchboard replacement, emergency short circuit fix.",
                location = "Gandhi Chowk, Near Metro",
                isVerified = true,
                isAvailable = true
            ),

            // Plumbers (Matching Screen 2 in Helpsetu.png)
            Worker(
                id = "w3",
                name = "MOHAN KUMAR",
                phone = "+919823456789",
                category = ServiceCategory.PLUMBER.id,
                categoryHindi = ServiceCategory.PLUMBER.titleHindi,
                rating = 4.9,
                jobsCompleted = 24,
                distanceKm = 1.2,
                experienceYears = 6,
                hourlyRate = "₹250/hr",
                workDetails = "Water tank cleaning, tap leakage, bathroom sanitary fittings & motor installation.",
                location = "Civil Lines, Block B",
                isVerified = true,
                isAvailable = true
            ),
            Worker(
                id = "w4",
                name = "RAKESH VERMA",
                phone = "+919834567890",
                category = ServiceCategory.PLUMBER.id,
                categoryHindi = ServiceCategory.PLUMBER.titleHindi,
                rating = 4.8,
                jobsCompleted = 19,
                distanceKm = 2.8,
                experienceYears = 5,
                hourlyRate = "₹200/hr",
                workDetails = "Drain blockage removal, pipeline fitting, RO purifier connection.",
                location = "Adarsh Nagar, Street 4",
                isVerified = true,
                isAvailable = true
            ),
            Worker(
                id = "w4b",
                name = "SITA DEVI",
                phone = "+919834567899",
                category = ServiceCategory.PLUMBER.id,
                categoryHindi = ServiceCategory.PLUMBER.titleHindi,
                rating = 4.9,
                jobsCompleted = 31,
                distanceKm = 3.1,
                experienceYears = 7,
                hourlyRate = "₹220/hr",
                workDetails = "Sanitary pipe maintenance, leak detection, kitchen sink repair.",
                location = "South Extension, Sector 2",
                isVerified = true,
                isAvailable = true
            ),

            // Labourers
            Worker(
                id = "w5",
                name = "Ram Avtar & Team",
                phone = "+919845678901",
                category = ServiceCategory.LABOURER.id,
                categoryHindi = ServiceCategory.LABOURER.titleHindi,
                rating = 4.9,
                jobsCompleted = 54,
                distanceKm = 0.5,
                experienceYears = 10,
                hourlyRate = "₹600/day",
                workDetails = "Household luggage shifting, construction helper, debris cleaning & garden digging.",
                location = "Shanti Nagar, Ring Road",
                isVerified = true,
                isAvailable = true
            ),
            Worker(
                id = "w6",
                name = "Mukesh Yadav",
                phone = "+919856789012",
                category = ServiceCategory.LABOURER.id,
                categoryHindi = ServiceCategory.LABOURER.titleHindi,
                rating = 4.7,
                jobsCompleted = 31,
                distanceKm = 1.9,
                experienceYears = 6,
                hourlyRate = "₹550/day",
                workDetails = "Truck unloading, heavy furniture movement, house renovation labor.",
                location = "Transport Nagar",
                isVerified = true,
                isAvailable = true
            ),

            // Carpenters
            Worker(
                id = "w7",
                name = "Harpreet Singh Mistri",
                phone = "+919867890123",
                category = ServiceCategory.CARPENTER.id,
                categoryHindi = ServiceCategory.CARPENTER.titleHindi,
                rating = 4.9,
                jobsCompleted = 47,
                distanceKm = 1.4,
                experienceYears = 12,
                hourlyRate = "₹350/hr",
                workDetails = "Modular kitchen repair, door locks, bed & sofa woodwork, custom cabinets.",
                location = "Wood Market, Sector 8",
                isVerified = true,
                isAvailable = true
            ),
            Worker(
                id = "w8",
                name = "Babloo Vishwakarma",
                phone = "+919878901234",
                category = ServiceCategory.CARPENTER.id,
                categoryHindi = ServiceCategory.CARPENTER.titleHindi,
                rating = 4.8,
                jobsCompleted = 26,
                distanceKm = 2.0,
                experienceYears = 6,
                hourlyRate = "₹300/hr",
                workDetails = "Door hinges, window mesh, wooden polishing, table & chair repair.",
                location = "Lajpat Nagar, Block C",
                isVerified = true,
                isAvailable = true
            )
        )
    }

    private fun getInitialRequests(): List<ServiceRequest> {
        return listOf(
            ServiceRequest(
                id = "req_1",
                workerId = "w1",
                workerName = "Ramesh Kumar Sharma",
                workerPhone = "+919876543210",
                workerCategory = ServiceCategory.ELECTRICIAN.id,
                customerPhone = "Self",
                contactType = "CALL",
                statusTag = "Contacted",
                timestamp = System.currentTimeMillis() - 3600000L * 2,
                notes = "Inquiry for fan repair"
            ),
            ServiceRequest(
                id = "req_2",
                workerId = "w3",
                workerName = "Suresh Patel",
                workerPhone = "+919823456789",
                workerCategory = ServiceCategory.PLUMBER.id,
                customerPhone = "Self",
                contactType = "WHATSAPP",
                statusTag = "WhatsApped",
                timestamp = System.currentTimeMillis() - 86400000L,
                notes = "Shared photo of leaking kitchen pipe"
            )
        )
    }

    companion object {
        @Volatile
        private var instance: WorkerRepository? = null

        fun getInstance(): WorkerRepository {
            return instance ?: synchronized(this) {
                instance ?: WorkerRepository().also { instance = it }
            }
        }
    }
}
