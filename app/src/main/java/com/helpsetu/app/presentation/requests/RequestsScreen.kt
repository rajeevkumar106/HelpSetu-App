package com.helpsetu.app.presentation.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Blue100
import com.example.ui.theme.Blue600
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.HelpSetuBackground
import com.example.ui.theme.HelpSetuCallBlue
import com.example.ui.theme.HelpSetuGreen
import com.example.ui.theme.HelpSetuGreenDark
import com.example.ui.theme.HelpSetuGreenLight
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
import com.helpsetu.app.data.model.ServiceRequest
import com.helpsetu.app.presentation.common.HelpSetuLocationDialog
import com.helpsetu.app.presentation.common.HelpSetuNotificationsDialog
import com.helpsetu.app.presentation.common.HelpSetuTopBar
import com.helpsetu.app.presentation.worker.WorkerViewModel
import com.helpsetu.app.utils.IntentHelpers

@Composable
fun RequestsScreen(
    viewModel: WorkerViewModel,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val requests by viewModel.serviceRequests.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "CALL", "WHATSAPP"
    var showLocationDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val filteredList = when (selectedFilter) {
        "CALL" -> requests.filter { it.contactType.equals("CALL", ignoreCase = true) }
        "WHATSAPP" -> requests.filter { it.contactType.equals("WHATSAPP", ignoreCase = true) }
        else -> requests
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HelpSetuBackground)
            .testTag("requests_screen")
    ) {
        // Top Brand Header Bar: Logo, Name, Tagline, Location, Notifications
        HelpSetuTopBar(
            subtitle = "Find Trusted Local Help Instantly",
            currentLocation = currentLocation,
            unreadNotificationsCount = unreadNotificationsCount,
            onLocationClick = { showLocationDialog = true },
            onNotificationsClick = { showNotificationsDialog = true }
        )

        // Header Section (Sleek Interface)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            border = BorderStroke(1.dp, Slate100),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My Requests / मेरे संपर्क",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "History of workers you contacted directly",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald50,
                        border = BorderStroke(1.dp, Emerald100)
                    ) {
                        Text(
                            text = "${requests.size} Total",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald600,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == "ALL",
                        onClick = { selectedFilter = "ALL" },
                        label = { Text("All Contacts (${requests.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HelpSetuGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Slate600
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == "ALL",
                            borderColor = if (selectedFilter == "ALL") Color.Transparent else Slate200
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    FilterChip(
                        selected = selectedFilter == "CALL",
                        onClick = { selectedFilter = "CALL" },
                        label = { Text("Calls", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HelpSetuGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Slate600
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == "CALL",
                            borderColor = if (selectedFilter == "CALL") Color.Transparent else Slate200
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    FilterChip(
                        selected = selectedFilter == "WHATSAPP",
                        onClick = { selectedFilter = "WHATSAPP" },
                        label = { Text("WhatsApp", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HelpSetuWhatsApp,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Slate600
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == "WHATSAPP",
                            borderColor = if (selectedFilter == "WHATSAPP") Color.Transparent else Slate200
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // List Content
        if (filteredList.isEmpty()) {
            EmptyRequestsState(
                filter = selectedFilter,
                onBrowseWorkers = onNavigateToHome
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .testTag("requests_list_column"),
                contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.id }) { request ->
                    RequestLogCard(
                        request = request,
                        onReCall = {
                            IntentHelpers.makePhoneCall(context, request.workerPhone)
                        },
                        onReMessage = {
                            val msg = "Hello ${request.workerName}, following up on my previous HelpSetu request."
                            IntentHelpers.openWhatsApp(context, request.workerPhone, msg)
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
fun RequestLogCard(
    request: ServiceRequest,
    onReCall: () -> Unit,
    onReMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWhatsApp = request.contactType.equals("WHATSAPP", ignoreCase = true)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("request_item_${request.id}"),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Slate100),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Initial Circle
                val initial = request.workerName.firstOrNull()?.uppercase() ?: "W"
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isWhatsApp) Emerald100 else Slate100),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isWhatsApp) HelpSetuGreen else Slate700
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.workerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Text(
                        text = "${request.workerCategory} • ${request.workerPhone}",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }

                // Status Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isWhatsApp) Emerald50 else Blue100.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, if (isWhatsApp) Emerald100 else Blue100)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = if (isWhatsApp) Icons.Outlined.Chat else Icons.Filled.Phone,
                            contentDescription = null,
                            tint = if (isWhatsApp) Emerald600 else Blue600,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = request.statusTag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWhatsApp) Emerald600 else Blue600
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time & Notes
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Contacted on ${request.formattedTime}",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Actions: Re-call & Re-message
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onReCall,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("recall_button_${request.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Slate200),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = HelpSetuGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Re-call",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Re-call",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onReMessage,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("remessage_button_${request.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Slate200),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = HelpSetuWhatsApp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Chat,
                        contentDescription = "Re-message",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Re-message",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyRequestsState(
    filter: String,
    onBrowseWorkers: () -> Unit,
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
                .background(Emerald100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
                tint = HelpSetuGreen,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Contacts Logged Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "When you call or WhatsApp any local electrician, plumber, labourer or carpenter, your contact history will be saved here automatically for quick re-calling.",
            fontSize = 13.sp,
            color = Slate500,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onBrowseWorkers,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HelpSetuGreen),
            modifier = Modifier.testTag("browse_workers_button")
        ) {
            Text("Find Local Workers Now", fontWeight = FontWeight.Bold)
        }
    }
}
