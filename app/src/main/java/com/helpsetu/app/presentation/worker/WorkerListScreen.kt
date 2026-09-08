package com.helpsetu.app.presentation.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber600
import com.example.ui.theme.Blue100
import com.example.ui.theme.Blue600
import com.example.ui.theme.CarpenterCardBg
import com.example.ui.theme.CarpenterCardBorder
import com.example.ui.theme.ElectricianCardBg
import com.example.ui.theme.ElectricianCardBorder
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.HelpSetuAmber
import com.example.ui.theme.HelpSetuAmberLight
import com.example.ui.theme.HelpSetuBackground
import com.example.ui.theme.HelpSetuGreen
import com.example.ui.theme.HelpSetuGreenDark
import com.example.ui.theme.HelpSetuGreenLight
import com.example.ui.theme.HelpSetuOrange
import com.example.ui.theme.HelpSetuTeal
import com.example.ui.theme.HelpSetuTealDark
import com.example.ui.theme.HelpSetuTextPrimary
import com.example.ui.theme.HelpSetuTextSecondary
import com.example.ui.theme.HelpSetuTextTertiary
import com.example.ui.theme.HelpSetuWhatsApp
import com.example.ui.theme.LabourerCardBg
import com.example.ui.theme.LabourerCardBorder
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange600
import com.example.ui.theme.PlumberCardBg
import com.example.ui.theme.PlumberCardBorder
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
import com.helpsetu.app.presentation.common.HelpSetuBrandHeader
import com.helpsetu.app.presentation.common.HelpSetuLocationDialog
import com.helpsetu.app.presentation.common.HelpSetuLogoIcon
import com.helpsetu.app.presentation.common.HelpSetuNotificationsDialog
import com.helpsetu.app.presentation.common.HelpSetuTopBar

/**
 * Screen 1: Category Selection (Home - 2x2 Grid View)
 * Matches Screen 1 in Helpsetu.png
 */
@Composable
fun CategorySelectionScreen(
    viewModel: WorkerViewModel,
    onCategorySelected: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRequests: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val workers by viewModel.workers.collectAsState()
    val requests by viewModel.serviceRequests.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HelpSetuBackground)
            .testTag("category_selection_screen")
    ) {
        // Top Brand Header (Emerald Gradient Banner with Logo, Name, Tagline, Location, Notifications)
        HelpSetuBrandHeader(
            currentLocation = currentLocation,
            selectedLanguage = selectedLanguage,
            unreadCount = unreadNotificationsCount,
            onLocationClick = { showLocationDialog = true },
            onLanguageClick = { viewModel.toggleLanguage() },
            onNotificationClick = { showNotificationsDialog = true },
            onHelplineClick = { viewModel.callHelpline(context) }
        )

        // Scrollable Home Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.setSearchQuery(it)
                    if (it.isNotBlank()) {
                        onCategorySelected("")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_bar"),
                placeholder = {
                    Text(
                        text = "Search services or workers...",
                        fontSize = 14.sp,
                        color = Slate400
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HelpSetuGreen,
                    unfocusedBorderColor = Slate200,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Category Grid (Screen 1) - 2x2 Grid from Helpsetu.png
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Electrician
                    CategoryGridCard(
                        category = ServiceCategory.ELECTRICIAN,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.selectCategory(ServiceCategory.ELECTRICIAN.id)
                            onCategorySelected(ServiceCategory.ELECTRICIAN.id)
                        }
                    )

                    // 2. Plumber
                    CategoryGridCard(
                        category = ServiceCategory.PLUMBER,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.selectCategory(ServiceCategory.PLUMBER.id)
                            onCategorySelected(ServiceCategory.PLUMBER.id)
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 3. Labourer
                    CategoryGridCard(
                        category = ServiceCategory.LABOURER,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.selectCategory(ServiceCategory.LABOURER.id)
                            onCategorySelected(ServiceCategory.LABOURER.id)
                        }
                    )

                    // 4. Carpenter
                    CategoryGridCard(
                        category = ServiceCategory.CARPENTER,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.selectCategory(ServiceCategory.CARPENTER.id)
                            onCategorySelected(ServiceCategory.CARPENTER.id)
                        }
                    )
                }
            }

            // Pagination Dots Indicator (Matching Screen 1 in Helpsetu.png)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active dot
                Box(
                    modifier = Modifier
                        .size(width = 18.dp, height = 7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(HelpSetuGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Inactive dot 1
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Slate200)
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Inactive dot 2
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Slate200)
                )
            }

            // Recent Contacts (Screen 3 Preview)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT CONTACTS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "VIEW ALL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpSetuTeal,
                        modifier = Modifier
                            .clickable { onNavigateToRequests() }
                            .padding(4.dp)
                            .testTag("home_view_all_requests")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (requests.isNotEmpty()) {
                    val recent = requests.first()
                    RecentContactCard(
                        name = recent.workerName,
                        subtitle = "${recent.workerCategory} • Contacted ${recent.formattedTime}",
                        tag = recent.statusTag,
                        initial = recent.workerName.firstOrNull()?.uppercase() ?: "W",
                        onClick = { onNavigateToRequests() }
                    )
                } else {
                    RecentContactCard(
                        name = "Mohan Kumar",
                        subtitle = "Plumber • Contacted 2h ago",
                        tag = "CONTACTED",
                        initial = "M",
                        onClick = { onNavigateToRequests() }
                    )
                }
            }

            // Register as Worker Callout Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToRegister() }
                    .testTag("worker_registration_prompt"),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Slate200),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Emerald100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Engineering,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Are you a skilled worker? / कामगार हैं?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                        Text(
                            text = "Register for free & receive customer calls",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                    Text(
                        text = "Register >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpSetuTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }

    // Interactive Dialogs for Top Bar
    HelpSetuLocationDialog(
        isOpen = showLocationDialog,
        currentLocation = currentLocation,
        onDismiss = { showLocationDialog = false },
        onSelectLocation = { viewModel.updateLocation(it) }
    )

    HelpSetuNotificationsDialog(
        isOpen = showNotificationsDialog,
        notifications = notifications,
        onDismiss = { showNotificationsDialog = false },
        onClearAll = { viewModel.clearNotifications() }
    )
}

@Composable
fun RecentContactCard(
    name: String,
    subtitle: String,
    tag: String,
    initial: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("home_recent_contact_card"),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Slate200),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate600
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Slate500
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Emerald50,
                border = androidx.compose.foundation.BorderStroke(1.dp, Emerald100)
            ) {
                Text(
                    text = tag.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald600,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryGridCard(
    category: ServiceCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    availableCount: Int = 0
) {
    val (bgColor, borderColor) = when (category) {
        ServiceCategory.ELECTRICIAN -> Pair(ElectricianCardBg, ElectricianCardBorder)
        ServiceCategory.PLUMBER -> Pair(PlumberCardBg, PlumberCardBorder)
        ServiceCategory.LABOURER -> Pair(LabourerCardBg, LabourerCardBorder)
        ServiceCategory.CARPENTER -> Pair(CarpenterCardBg, CarpenterCardBorder)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() }
            .testTag("category_card_${category.id}"),
        shape = RoundedCornerShape(24.dp),
        color = bgColor,
        border = BorderStroke(1.5.dp, borderColor),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Illustrated Graphic matching Helpsetu.png Screen 1
            CategoryIllustratedIcon(
                category = category,
                size = 64.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Primary English Label (Uppercase)
            Text(
                text = category.titleEnglish.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Subtitle Hindi Label
            Text(
                text = category.titleHindi,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Slate700,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Screen 2: Results & Connection (Workers List)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerListScreen(
    viewModel: WorkerViewModel,
    onBackToCategories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredWorkers by viewModel.filteredWorkers.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HelpSetuBackground)
            .testTag("worker_list_screen")
    ) {
        // Top Brand Bar: Logo, Name, Tagline, Location, Notifications & Back
        HelpSetuTopBar(
            onBack = onBackToCategories,
            backButtonTag = "back_to_categories_button",
            subtitle = "Find Trusted Local Help Instantly",
            currentLocation = currentLocation,
            unreadNotificationsCount = unreadNotificationsCount,
            onLocationClick = { showLocationDialog = true },
            onNotificationsClick = { showNotificationsDialog = true }
        )

        // Sub-Header: "Your Request" Category Pill & Search Refinement
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            border = BorderStroke(1.dp, Slate100),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                val currentCategoryTitle = when {
                    selectedCategory.equals(ServiceCategory.ELECTRICIAN.id, ignoreCase = true) -> "Electrician"
                    selectedCategory.equals(ServiceCategory.PLUMBER.id, ignoreCase = true) -> "Plumber"
                    selectedCategory.equals(ServiceCategory.LABOURER.id, ignoreCase = true) -> "Labourer"
                    selectedCategory.equals(ServiceCategory.CARPENTER.id, ignoreCase = true) -> "Carpenter"
                    searchQuery.isNotBlank() -> searchQuery
                    else -> "Local Service"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Emerald50,
                        border = BorderStroke(1.dp, Emerald100)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Build,
                                contentDescription = null,
                                tint = HelpSetuTeal,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Your Request: ",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                            Text(
                                text = currentCategoryTitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HelpSetuTeal
                            )
                        }
                    }

                    Text(
                        text = "${filteredWorkers.size} available",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate500
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_search_input"),
                    placeholder = {
                        Text(
                            text = "Search services or workers...",
                            fontSize = 13.sp,
                            color = Slate400
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear search",
                                    tint = Slate400,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Horizontal Category Filter Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_filter_chips_row"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("All (${filteredWorkers.size})", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HelpSetuTeal,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Slate600
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == null,
                                borderColor = if (selectedCategory == null) Color.Transparent else Slate200
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    items(ServiceCategory.values()) { cat ->
                        val isSelected = selectedCategory.equals(cat.id, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) viewModel.selectCategory(null)
                                else viewModel.selectCategory(cat.id)
                            },
                            label = {
                                Text(
                                    text = "${cat.titleEnglish} / ${cat.titleHindi}",
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HelpSetuTeal,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Slate600
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) Color.Transparent else Slate200
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
        }

        // Section Title matching Screen 2 in Helpsetu.png: "{Category} Near You"
        val sectionTitle = when {
            selectedCategory.equals(ServiceCategory.PLUMBER.id, ignoreCase = true) -> "Plumbers Near You"
            selectedCategory.equals(ServiceCategory.ELECTRICIAN.id, ignoreCase = true) -> "Electricians Near You"
            selectedCategory.equals(ServiceCategory.LABOURER.id, ignoreCase = true) -> "Labourers Near You"
            selectedCategory.equals(ServiceCategory.CARPENTER.id, ignoreCase = true) -> "Carpenters Near You"
            searchQuery.isNotBlank() -> "Results for \"$searchQuery\""
            else -> "Workers Near You"
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = sectionTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Direct connection • No middleman • Free call",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        }

        // Action Feedback Notification Banner
        AnimatedVisibility(visible = actionMessage != null) {
            actionMessage?.let { msg ->
                Surface(
                    color = HelpSetuGreenDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearActionMessage() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Dismiss",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Workers List or Empty State
        if (filteredWorkers.isEmpty()) {
            EmptyWorkersState(
                searchQuery = searchQuery,
                onClearFilter = {
                    viewModel.selectCategory(null)
                    viewModel.setSearchQuery("")
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .testTag("workers_list_column"),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredWorkers, key = { it.id }) { worker ->
                    WorkerCard(
                        worker = worker,
                        onCall = {
                            viewModel.contactWorker(context, worker, "CALL")
                        },
                        onWhatsApp = {
                            viewModel.contactWorker(context, worker, "WHATSAPP")
                        }
                    )
                }
            }
        }
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

@Composable
fun WorkerCard(
    worker: Worker,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("worker_card_${worker.id}"),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Avatar with verified indicator, Name, Aadhaar Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar with verified badge overlaid at corner
                Box(modifier = Modifier.size(52.dp)) {
                    val initial = worker.name.firstOrNull()?.uppercase() ?: "W"
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Emerald100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initial,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = HelpSetuTeal
                        )
                    }

                    if (worker.isVerified) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = HelpSetuGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = worker.name.uppercase(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(${worker.category})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate500
                            )
                        }

                        // Aadhaar Verified Badge from Helpsetu.png
                        AadhaarVerifiedBadge()
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Rating & Distance Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Rating
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = HelpSetuAmber,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${worker.rating}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )
                        Text(
                            text = " (${worker.jobsCompleted} jobs)",
                            fontSize = 11.sp,
                            color = Slate500
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "•", fontSize = 12.sp, color = Slate400)
                        Spacer(modifier = Modifier.width(10.dp))

                        // Distance
                        Icon(
                            imageVector = Icons.Filled.NearMe,
                            contentDescription = null,
                            tint = HelpSetuTeal,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${worker.distanceKm} KM AWAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HelpSetuTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Work details / Tagline
            if (worker.workDetails.isNotBlank()) {
                Text(
                    text = worker.workDetails,
                    fontSize = 12.sp,
                    color = Slate600,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Location & Rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = worker.location,
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Text(
                    text = "${worker.hourlyRate} • ${worker.experienceYears} yrs exp",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HelpSetuTeal
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two Action Buttons per card matching Helpsetu.png Screen 2:
            // "CALL NOW" (Solid Pill Teal) and "WHATSAPP" (Outlined Pill Teal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CALL NOW Solid Pill Button
                Button(
                    onClick = onCall,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("call_now_button_${worker.id}"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HelpSetuTeal
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CALL NOW",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.4.sp
                    )
                }

                // WHATSAPP Outlined Pill Button
                OutlinedButton(
                    onClick = onWhatsApp,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("whatsapp_button_${worker.id}"),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, HelpSetuTeal),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = HelpSetuTeal
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = "WhatsApp",
                        tint = HelpSetuTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WHATSAPP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpSetuTeal,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyWorkersState(
    searchQuery: String,
    onClearFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(HelpSetuGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = HelpSetuGreen,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Workers Found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HelpSetuTextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (searchQuery.isNotBlank())
                "No service provider matching \"$searchQuery\". Try checking spelling or reset filters."
            else
                "No workers available in this category right now.",
            fontSize = 13.sp,
            color = HelpSetuTextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onClearFilter,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("reset_filters_button")
        ) {
            Text("Clear Search & Filters", color = HelpSetuGreen, fontWeight = FontWeight.Bold)
        }
    }
}
