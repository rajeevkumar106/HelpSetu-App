package com.helpsetu.app.presentation.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.HelpSetuAmber
import com.example.ui.theme.HelpSetuBackground
import com.example.ui.theme.HelpSetuError
import com.example.ui.theme.HelpSetuGreen
import com.example.ui.theme.HelpSetuGreenDark
import com.example.ui.theme.HelpSetuOrange
import com.example.ui.theme.HelpSetuTeal
import com.example.ui.theme.HelpSetuTealDark
import com.example.ui.theme.HelpSetuWhatsApp
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.helpsetu.app.data.model.ServiceCategory
import com.helpsetu.app.data.model.Worker
import com.helpsetu.app.presentation.common.AadhaarVerifiedBadge
import com.helpsetu.app.presentation.common.CategoryIllustratedIcon
import com.helpsetu.app.presentation.common.HelpSetuLogoIcon
import com.helpsetu.app.presentation.common.HelpSetuLocationDialog
import com.helpsetu.app.presentation.common.HelpSetuNotificationsDialog
import com.helpsetu.app.presentation.common.HelpSetuWordmark
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox

enum class ProfileScreenMode {
    VIEW,   // Read: show detailed profile cards
    CREATE, // Create: form to register new profile
    EDIT    // Update: form to edit selected profile
}

/**
 * Screen 4: Profile Section with Full CRUD Operations & Detailed Profile View
 * - Top Bar: Logo, Name, Tagline, Location selector, Notifications, Language
 * - Create: Register new service profile
 * - Read: Display complete profile details with verified badge, stats, and contact info
 * - Update: Modify existing profile details and toggle live availability
 * - Delete: Remove profile with confirmation dialog
 */
@Composable
fun EditProfileScreen(
    viewModel: WorkerViewModel,
    onNavigateToWorkers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentProfile by viewModel.currentProfile.collectAsState()
    val myWorkerProfiles by viewModel.myWorkerProfiles.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var screenMode by remember { mutableStateOf(ProfileScreenMode.VIEW) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HelpSetuBackground)
            .testTag("profile_section_screen")
    ) {
        // 1. Primary Brand Top Bar (Logo, Name, Tagline, Location, Notifications)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Brand Logo + Name + Tagline
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        // Circular Logo Icon
                        Surface(
                            shape = CircleShape,
                            color = Emerald50,
                            border = BorderStroke(1.dp, Emerald100),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                HelpSetuLogoIcon(size = 24.dp, isOnDark = false)
                            }
                        }

                        Spacer(modifier = Modifier.width(9.dp))

                        Column {
                            HelpSetuWordmark(fontSize = 19.sp, isOnDark = false)
                            Text(
                                text = "Find Trusted Local Help Instantly",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate500,
                                maxLines = 1
                            )
                        }
                    }

                    // Right Side: Location Chip, Language, Notifications
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Location Chip
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Emerald50,
                            border = BorderStroke(1.dp, Emerald100),
                            modifier = Modifier
                                .clickable { showLocationDialog = true }
                                .testTag("profile_topbar_location_selector")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Location",
                                    tint = HelpSetuTeal,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = currentLocation.split(",").first().trim(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = HelpSetuTealDark,
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    tint = HelpSetuTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // Notifications Icon with Badge
                        IconButton(
                            onClick = { showNotificationsDialog = true },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("profile_topbar_notifications_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotificationsCount > 0) {
                                        Badge(containerColor = HelpSetuOrange) {
                                            Text(text = "$unreadNotificationsCount", fontSize = 9.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Slate700,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Profile Screen Action Sub-Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Slate50,
            border = BorderStroke(1.dp, Slate200)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (screenMode != ProfileScreenMode.VIEW) {
                        IconButton(
                            onClick = { screenMode = ProfileScreenMode.VIEW },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("profile_back_to_view_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to View",
                                tint = Slate800
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Column {
                        Text(
                            text = when (screenMode) {
                                ProfileScreenMode.VIEW -> if (currentProfile != null) "Worker Profile" else "Worker Registration"
                                ProfileScreenMode.CREATE -> "Register as Worker"
                                ProfileScreenMode.EDIT -> "Edit Profile Details"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = (-0.2).sp
                        )
                        Text(
                            text = when (screenMode) {
                                ProfileScreenMode.VIEW -> if (currentProfile != null) "Your registered service profile • कामगार प्रोफ़ाइल" else "पंजीकरण करें • Register to get direct calls"
                                ProfileScreenMode.CREATE -> "सेवा प्रदाता बनें (नया प्रोफ़ाइल)"
                                ProfileScreenMode.EDIT -> "प्रोफ़ाइल विवरण अपडेट करें"
                            },
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                // Top Right Action: "+ New Profile" button when in VIEW mode
                if (screenMode == ProfileScreenMode.VIEW) {
                    Button(
                        onClick = { screenMode = ProfileScreenMode.CREATE },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = HelpSetuTeal),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("create_new_profile_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (myWorkerProfiles.isEmpty()) "Register" else "New",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Action Feedback Notification Banner
        AnimatedVisibility(visible = actionMessage != null) {
            actionMessage?.let { msg ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("profile_action_banner"),
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald100)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = HelpSetuGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                fontSize = 12.sp,
                                color = Slate800,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearActionMessage() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Dismiss",
                                tint = Slate500,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Profiles Switcher (Horizontal Chips) - ONLY user-created worker profiles
        if (myWorkerProfiles.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Slate100))
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "YOUR WORKER PROFILES (${myWorkerProfiles.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 0.5.sp
                    )
                    if (myWorkerProfiles.size > 1) {
                        Text(
                            text = "Tap to switch profile",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(myWorkerProfiles, key = { it.id }) { worker ->
                        val isSelected = currentProfile?.id == worker.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.selectProfile(worker.id)
                                screenMode = ProfileScreenMode.VIEW
                            },
                            label = {
                                Text(
                                    text = "${worker.name.substringBefore(" ")} • ${worker.category}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (worker.isAvailable) HelpSetuGreen else Slate400)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HelpSetuTeal,
                                selectedLabelColor = Color.White,
                                containerColor = Slate50,
                                labelColor = Slate700
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) HelpSetuTeal else Slate200
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("profile_chip_${worker.id}")
                        )
                    }

                    // Quick "+ Add Profile" Chip
                    item {
                        FilterChip(
                            selected = screenMode == ProfileScreenMode.CREATE,
                            onClick = { screenMode = ProfileScreenMode.CREATE },
                            label = { Text("+ Add Profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Emerald50,
                                labelColor = HelpSetuTeal,
                                selectedContainerColor = HelpSetuTeal,
                                selectedLabelColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Emerald100),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("add_profile_chip")
                        )
                    }
                }
            }
        }

        // Screen Body based on Mode
        when (screenMode) {
            ProfileScreenMode.VIEW -> {
                if (currentProfile != null) {
                    ProfileDetailsView(
                        worker = currentProfile!!,
                        onEdit = { screenMode = ProfileScreenMode.EDIT },
                        onDelete = { showDeleteDialog = true },
                        onToggleAvailability = {
                            currentProfile?.let { viewModel.toggleProfileAvailability(it.id) }
                        },
                        onNavigateToWorkers = {
                            currentProfile?.let { viewModel.selectCategory(it.category) }
                            onNavigateToWorkers()
                        },
                        onContactCall = {
                            currentProfile?.let { viewModel.contactWorker(context, it, "CALL") }
                        },
                        onContactWhatsApp = {
                            currentProfile?.let { viewModel.contactWorker(context, it, "WHATSAPP") }
                        }
                    )
                } else {
                    ProfileEmptyView(
                        onCreateNew = { screenMode = ProfileScreenMode.CREATE },
                        onBrowseWorkers = onNavigateToWorkers
                    )
                }
            }

            ProfileScreenMode.CREATE -> {
                ProfileForm(
                    title = "Register New Service Profile",
                    subtitle = "Fill in your trade details to receive local job inquiries",
                    initialWorker = null,
                    onSave = { name, phone, category, details, location, rate, experience, isAvailable ->
                        val success = viewModel.createProfile(
                            name = name,
                            phone = phone,
                            category = category,
                            workDetails = details,
                            location = location,
                            hourlyRate = rate,
                            experienceYears = experience,
                            isAvailable = isAvailable
                        )
                        if (success) {
                            screenMode = ProfileScreenMode.VIEW
                        }
                    },
                    onCancel = { screenMode = ProfileScreenMode.VIEW }
                )
            }

            ProfileScreenMode.EDIT -> {
                ProfileForm(
                    title = "Edit Service Profile",
                    subtitle = "Update details for ${currentProfile?.name ?: "worker"}",
                    initialWorker = currentProfile,
                    onSave = { name, phone, category, details, location, rate, experience, isAvailable ->
                        val id = currentProfile?.id ?: return@ProfileForm
                        val success = viewModel.updateProfile(
                            id = id,
                            name = name,
                            phone = phone,
                            category = category,
                            workDetails = details,
                            location = location,
                            hourlyRate = rate,
                            experienceYears = experience,
                            isAvailable = isAvailable
                        )
                        if (success) {
                            screenMode = ProfileScreenMode.VIEW
                        }
                    },
                    onCancel = { screenMode = ProfileScreenMode.VIEW }
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && currentProfile != null) {
        val workerToDelete = currentProfile!!
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = HelpSetuError,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Delete Profile?", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to delete profile for \"${workerToDelete.name}\" (${workerToDelete.category})? This will permanently remove your listing from HelpSetu search results.",
                    fontSize = 14.sp,
                    color = Slate700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProfile(workerToDelete.id)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HelpSetuError),
                    modifier = Modifier.testTag("confirm_delete_profile_button")
                ) {
                    Text("Delete Permanently", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    modifier = Modifier.testTag("cancel_delete_profile_button")
                ) {
                    Text("Cancel", color = Slate700)
                }
            }
        )
    }

    // Location Selection Dialog
    HelpSetuLocationDialog(
        isOpen = showLocationDialog,
        currentLocation = currentLocation,
        onDismiss = { showLocationDialog = false },
        onSelectLocation = { viewModel.updateLocation(it) }
    )

    // Notifications Dialog
    HelpSetuNotificationsDialog(
        isOpen = showNotificationsDialog,
        notifications = notifications,
        onDismiss = { showNotificationsDialog = false },
        onClearAll = { viewModel.clearNotifications() }
    )
}

/**
 * Detailed Profile View (READ Operation)
 * Displays rich worker profile details matching HelpSetu branding.
 */
@Composable
fun ProfileDetailsView(
    worker: Worker,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleAvailability: () -> Unit,
    onNavigateToWorkers: () -> Unit,
    onContactCall: () -> Unit,
    onContactWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val categoryEnum = ServiceCategory.fromId(worker.category) ?: ServiceCategory.ELECTRICIAN

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Profile Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_hero_card"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // My Worker Profile Badge
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald100),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "MY WORKER PROFILE • आपका कामगार प्रोफ़ाइल",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = HelpSetuTealDark,
                            letterSpacing = 0.4.sp
                        )
                    }
                }

                // Top Row: Avatar with status dot, Name, Aadhaar Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar & Illustration
                    Box(modifier = Modifier.size(68.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Emerald100),
                            contentAlignment = Alignment.Center
                        ) {
                            CategoryIllustratedIcon(
                                category = categoryEnum,
                                size = 44.dp
                            )
                        }

                        // Live status indicator dot
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(if (worker.isAvailable) HelpSetuGreen else Slate400)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = worker.name.uppercase(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Category Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Emerald50,
                            border = BorderStroke(1.dp, Emerald100)
                        ) {
                            Text(
                                text = "${worker.category} • ${worker.categoryHindi}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HelpSetuTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        AadhaarVerifiedBadge()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Availability Toggle Row
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = if (worker.isAvailable) Emerald50 else Slate50,
                    border = BorderStroke(1.dp, if (worker.isAvailable) Emerald100 else Slate200)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (worker.isAvailable) HelpSetuGreen else Slate400)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (worker.isAvailable) "Available for Work" else "Currently Busy",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (worker.isAvailable) Emerald600 else Slate600
                                )
                            }
                            Text(
                                text = if (worker.isAvailable) "Customers can call you now" else "Profile hidden from immediate calls",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }

                        Switch(
                            checked = worker.isAvailable,
                            onCheckedChange = { onToggleAvailability() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = HelpSetuGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Slate400
                            ),
                            modifier = Modifier.testTag("profile_availability_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary CRUD Action Buttons (EDIT & DELETE)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // EDIT BUTTON (Update Operation)
                    Button(
                        onClick = onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("edit_profile_details_button"),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = HelpSetuTeal),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EDIT PROFILE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.4.sp
                        )
                    }

                    // DELETE BUTTON (Delete Operation)
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("delete_profile_button"),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.5.dp, HelpSetuError),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = HelpSetuError
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = HelpSetuError,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DELETE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = HelpSetuError,
                            letterSpacing = 0.4.sp
                        )
                    }
                }
            }
        }

        // 2. Key Performance & Experience Stats (4-metric Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileStatCard(
                label = "RATING",
                value = "${worker.rating} ★",
                subtext = "Verified reviews",
                icon = Icons.Filled.Star,
                iconTint = HelpSetuAmber,
                modifier = Modifier.weight(1f)
            )
            ProfileStatCard(
                label = "JOBS DONE",
                value = "${worker.jobsCompleted}+",
                subtext = "Completed tasks",
                icon = Icons.Filled.CheckCircle,
                iconTint = HelpSetuGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileStatCard(
                label = "EXPERIENCE",
                value = "${worker.experienceYears} Yrs",
                subtext = "Industry practice",
                icon = Icons.Filled.WorkHistory,
                iconTint = HelpSetuTeal,
                modifier = Modifier.weight(1f)
            )
            ProfileStatCard(
                label = "SERVICE RATE",
                value = worker.hourlyRate,
                subtext = "Standard charge",
                icon = Icons.Filled.Payments,
                iconTint = HelpSetuGreenDark,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. Contact & Location Information Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Contact & Location Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // Phone Number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Emerald50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                tint = HelpSetuTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Registered Phone",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                            Text(
                                text = worker.phone,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                        }
                    }

                    // Quick test call action
                    IconButton(
                        onClick = onContactCall,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Test Call",
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider()

                // Location Area
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Emerald50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Work Area / Location",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                        Text(
                            text = worker.location,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                    }
                }

                HorizontalDivider()

                // Service Radius / Proximity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Emerald50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.NearMe,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Default Distance Radius",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                        Text(
                            text = "${worker.distanceKm} KM radius coverage",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                    }
                }
            }
        }

        // 4. Skills & Work Details Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = null,
                        tint = HelpSetuTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Work Details & Specializations",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Text(
                    text = if (worker.workDetails.isNotBlank()) worker.workDetails else "No additional description added yet.",
                    fontSize = 13.sp,
                    color = Slate700,
                    lineHeight = 19.sp
                )
            }
        }

        // 5. Preview in Customer Search Button
        Button(
            onClick = onNavigateToWorkers,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("preview_in_worker_list_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Slate800)
        ) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Preview in Worker Search / कामगार सूची में देखें",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

/**
 * Clean Single Stat Card
 */
@Composable
fun ProfileStatCard(
    label: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(104.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 0.5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    letterSpacing = 0.4.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            Text(
                text = subtext,
                fontSize = 10.sp,
                color = Slate400
            )
        }
    }
}

/**
 * Profile Form for CREATE and UPDATE Operations
 */
@Composable
fun ProfileForm(
    title: String,
    subtitle: String,
    initialWorker: Worker?,
    onSave: (
        name: String,
        phone: String,
        category: String,
        workDetails: String,
        location: String,
        hourlyRate: String,
        experienceYears: Int,
        isAvailable: Boolean
    ) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(initialWorker) { mutableStateOf(initialWorker?.name ?: "") }
    var phone by remember(initialWorker) { mutableStateOf(initialWorker?.phone ?: "") }
    var selectedCategory by remember(initialWorker) {
        mutableStateOf(initialWorker?.category ?: ServiceCategory.ELECTRICIAN.id)
    }
    var location by remember(initialWorker) { mutableStateOf(initialWorker?.location ?: "") }
    var hourlyRate by remember(initialWorker) { mutableStateOf(initialWorker?.hourlyRate ?: "₹250/hr") }
    var experienceYearsText by remember(initialWorker) {
        mutableStateOf(initialWorker?.experienceYears?.toString() ?: "5")
    }
    var workDetails by remember(initialWorker) { mutableStateOf(initialWorker?.workDetails ?: "") }
    var isAvailable by remember(initialWorker) { mutableStateOf(initialWorker?.isAvailable ?: true) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Emerald50,
            border = BorderStroke(1.dp, Emerald100)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Slate600
                )
            }
        }

        // Section 1: Personal & Contact
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "1. Personal & Contact Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Full Name (पूरा नाम) *") },
                    placeholder = { Text("e.g. Ramesh Kumar Sharma") },
                    leadingIcon = {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Slate400)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        errorMessage = null
                    },
                    label = { Text("Mobile Number (फोन नंबर) *") },
                    placeholder = { Text("+91 9876543210") },
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = Slate400)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_phone_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Work Location / Area (कार्य क्षेत्र)") },
                    placeholder = { Text("e.g. Sector 14, Main Market") },
                    leadingIcon = {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Slate400)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_location_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
        }

        // Section 2: Service Category Selection
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "2. Select Service Category (काम की श्रेणी) *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )

                ServiceCategory.values().forEach { cat ->
                    val isSelected = selectedCategory.equals(cat.id, ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Emerald50 else Color.Transparent)
                            .clickable { selectedCategory = cat.id }
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .testTag("category_radio_${cat.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedCategory = cat.id },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = HelpSetuTeal,
                                unselectedColor = Slate400
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        CategoryIllustratedIcon(
                            category = cat,
                            size = 32.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${cat.titleEnglish} / ${cat.titleHindi}",
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Slate900 else Slate700
                            )
                            Text(
                                text = cat.description,
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Pricing, Experience & Skills
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "3. Pricing, Experience & Skills",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = hourlyRate,
                        onValueChange = { hourlyRate = it },
                        label = { Text("Rate / शुल्क") },
                        placeholder = { Text("₹250/hr") },
                        leadingIcon = {
                            Icon(Icons.Filled.Payments, contentDescription = null, tint = Slate400)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("worker_rate_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HelpSetuTeal,
                            unfocusedBorderColor = Slate200,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = experienceYearsText,
                        onValueChange = { experienceYearsText = it },
                        label = { Text("Exp (Years)") },
                        placeholder = { Text("5") },
                        leadingIcon = {
                            Icon(Icons.Filled.WorkHistory, contentDescription = null, tint = Slate400)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("worker_exp_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HelpSetuTeal,
                            unfocusedBorderColor = Slate200,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                OutlinedTextField(
                    value = workDetails,
                    onValueChange = { workDetails = it },
                    label = { Text("Work Details & Skills (विशेषज्ञता)") },
                    placeholder = { Text("e.g. Complete house wiring, fan repair, MCB tripping, inverter repair...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Description, contentDescription = null, tint = Slate400)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("worker_details_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                // Availability Switch inside Form
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Set Profile as Active & Available",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800
                        )
                        Text(
                            text = "Customers can reach you directly via Call & WhatsApp",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }

                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = HelpSetuTeal
                        )
                    )
                }
            }
        }

        // Error message if any
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = HelpSetuError,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Form Action Buttons: Submit & Cancel
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name"
                        return@Button
                    }
                    if (phone.isBlank()) {
                        errorMessage = "Please enter your mobile number"
                        return@Button
                    }
                    val exp = experienceYearsText.toIntOrNull() ?: 1
                    onSave(
                        name,
                        phone,
                        selectedCategory,
                        workDetails,
                        if (location.isNotBlank()) location else "Local Area",
                        if (hourlyRate.isNotBlank()) hourlyRate else "₹250/hr",
                        exp,
                        isAvailable
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_worker_profile_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HelpSetuTeal)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (initialWorker == null) "CREATE PROFILE / प्रोफ़ाइल बनाएं" else "UPDATE PROFILE / अपडेट करें",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("cancel_form_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Text("Cancel", color = Slate700, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * View shown when no worker profiles have been created by the user yet
 */
@Composable
fun ProfileEmptyView(
    onCreateNew: () -> Unit,
    onBrowseWorkers: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Worker Emblem / Icon
        Surface(
            shape = CircleShape,
            color = Emerald50,
            border = BorderStroke(2.dp, Emerald100),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = HelpSetuTeal,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create Your Worker Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "कामगार के रूप में पंजीकरण करें • 100% Direct Calls",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = HelpSetuTealDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You haven't created a worker profile yet. Register your trade profile to start receiving direct job calls and WhatsApp inquiries from customers in your area.",
            fontSize = 13.sp,
            color = Slate500,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Benefits Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "WHY REGISTER ON HELPKSETU?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.5.sp
                )

                // Benefit 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Emerald50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = null,
                            tint = HelpSetuGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "0% Commission, 100% Direct Calls",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Customers call or WhatsApp you directly. You negotiate your own rate and keep 100% of your earnings.",
                            fontSize = 12.sp,
                            color = Slate500,
                            lineHeight = 16.sp
                        )
                    }
                }

                HorizontalDivider()

                // Benefit 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Emerald50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aadhaar KYC Verification Badge",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Stand out with verified trust credentials and get up to 3x more service inquiries from homeowners.",
                            fontSize = 12.sp,
                            color = Slate500,
                            lineHeight = 16.sp
                        )
                    }
                }

                HorizontalDivider()

                // Benefit 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Emerald50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkHistory,
                            contentDescription = null,
                            tint = HelpSetuAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Control Your Availability",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Toggle your status between Available and Busy anytime with a single tap.",
                            fontSize = 12.sp,
                            color = Slate500,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onCreateNew,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = HelpSetuTeal),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("create_first_profile_button")
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Register as a Worker / प्रोफ़ाइल बनाएं",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (onBrowseWorkers != null) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onBrowseWorkers,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("browse_workers_from_empty_profile_button")
            ) {
                Text(
                    text = "Looking for Help? Browse Workers (कामगार खोजें)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Slate100)
    )
}
