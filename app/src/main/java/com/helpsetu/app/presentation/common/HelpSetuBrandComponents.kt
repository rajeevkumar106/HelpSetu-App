package com.helpsetu.app.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
import com.example.ui.theme.HelpSetuGreenLight
import com.example.ui.theme.HelpSetuOrange
import com.example.ui.theme.HelpSetuTeal
import com.example.ui.theme.HelpSetuTealDark
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
import com.helpsetu.app.presentation.worker.AppNotification

/**
 * The 3-element icon from Helpsetu.png logo:
 * - Left: Teal/Green tool shape
 * - Middle: Amber lightbulb with glow rays
 * - Right: Orange spanner / tool
 */
@Composable
fun HelpSetuLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    isOnDark: Boolean = false
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Colors
        val greenColor = if (isOnDark) Color(0xFF6EE7B7) else Color(0xFF059669)
        val yellowColor = if (isOnDark) Color(0xFFFDE047) else Color(0xFFF59E0B)
        val orangeColor = if (isOnDark) Color(0xFFFDBA74) else Color(0xFFF97316)

        // 1. Left element: Whisk / loop tool (Green)
        val leftX = w * 0.22f
        drawCircle(
            color = greenColor,
            radius = w * 0.12f,
            center = Offset(leftX, h * 0.35f),
            style = Stroke(width = w * 0.08f)
        )
        drawLine(
            color = greenColor,
            start = Offset(leftX, h * 0.45f),
            end = Offset(leftX, h * 0.85f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )

        // 2. Middle element: Glowing lightbulb (Yellow)
        val midX = w * 0.50f
        drawCircle(
            color = yellowColor,
            radius = w * 0.16f,
            center = Offset(midX, h * 0.38f)
        )
        // Bulb base
        drawRect(
            color = yellowColor,
            topLeft = Offset(midX - w * 0.08f, h * 0.50f),
            size = androidx.compose.ui.geometry.Size(w * 0.16f, h * 0.22f)
        )
        // Bulb rays (3 small spark lines)
        val rayLen = w * 0.08f
        drawLine(
            color = yellowColor,
            start = Offset(midX, h * 0.14f),
            end = Offset(midX, h * 0.14f - rayLen),
            strokeWidth = w * 0.06f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = yellowColor,
            start = Offset(midX - w * 0.18f, h * 0.24f),
            end = Offset(midX - w * 0.25f, h * 0.18f),
            strokeWidth = w * 0.06f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = yellowColor,
            start = Offset(midX + w * 0.18f, h * 0.24f),
            end = Offset(midX + w * 0.25f, h * 0.18f),
            strokeWidth = w * 0.06f,
            cap = StrokeCap.Round
        )

        // 3. Right element: Orange wrench / roller tool
        val rightX = w * 0.78f
        drawCircle(
            color = orangeColor,
            radius = w * 0.10f,
            center = Offset(rightX, h * 0.30f),
            style = Stroke(width = w * 0.08f)
        )
        drawLine(
            color = orangeColor,
            start = Offset(rightX, h * 0.38f),
            end = Offset(rightX, h * 0.85f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Two-tone "HelpSetu" wordmark:
 * "Help" in Teal, "Setu" in Orange/Amber
 */
@Composable
fun HelpSetuWordmark(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    isOnDark: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Help",
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = if (isOnDark) Color.White else HelpSetuTeal,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Setu",
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = if (isOnDark) Color(0xFFFDE047) else HelpSetuOrange,
            letterSpacing = (-0.5).sp
        )
    }
}

/**
 * Full HelpSetu Brand Logo Header with complete topbar capabilities:
 * - Utility row: Location selector, Language switcher, Helpline call, Notification bell with badge
 * - Central Brand identity: 3-element Logo icon, two-tone "HelpSetu", official tagline in English and Hindi
 * - Bottom Trust assurance pills: 100% Direct, Aadhaar Verified, Zero Commission
 */
@Composable
fun HelpSetuBrandHeader(
    currentLocation: String = "Noida, Sector 62 (Delhi NCR)",
    selectedLanguage: String = "HI",
    unreadCount: Int = 2,
    onLocationClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onHelplineClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF047857), // Deep emerald
                            Color(0xFF059669), // Rich green
                            Color(0xFF10B981)  // Vibrant green
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Top Utility Bar: Location Selector & Action Utilities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location Selector Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x33000000),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                        modifier = Modifier
                            .clickable { onLocationClick() }
                            .testTag("topbar_location_selector")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFFFDE047), // Amber yellow
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLocation.split("(").first().trim(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Select Location",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Top Right Controls (Language, Helpline, Notifications)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Language Switcher Pill
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            modifier = Modifier
                                .clickable { onLanguageClick() }
                                .testTag("topbar_language_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Language,
                                    contentDescription = "Language",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (selectedLanguage == "HI") "हिंदी" else "English",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Helpline Call Button
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            modifier = Modifier
                                .clickable { onHelplineClick() }
                                .testTag("topbar_helpline_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Call,
                                    contentDescription = "24x7 Helpline",
                                    tint = Color(0xFFFDE047),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Help",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Notification Bell with Badge
                        Surface(
                            shape = CircleShape,
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onNotificationClick() }
                                .testTag("topbar_notifications_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                BadgedBox(
                                    badge = {
                                        if (unreadCount > 0) {
                                            Badge(
                                                containerColor = HelpSetuOrange,
                                                contentColor = Color.White
                                            ) {
                                                Text(text = "$unreadCount", fontSize = 9.sp)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Central Brand Identity: Logo + Two-tone Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Elevated Logo circular badge
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            HelpSetuLogoIcon(
                                size = 32.dp,
                                isOnDark = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Brand Name
                    HelpSetuWordmark(
                        fontSize = 30.sp,
                        isOnDark = true
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 3. Official Taglines (English + Hindi)
                Text(
                    text = "Find Trusted Local Help Instantly",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.2.sp
                )
                Text(
                    text = "विश्वसनीय स्थानीय कामगार तुरंत खोजें • Direct Connection",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFD1FAE5), // Light mint emerald
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Trust Assurance Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TrustPill(icon = Icons.Filled.Call, label = "Direct Call & Chat")
                    TrustPill(icon = Icons.Filled.Verified, label = "Aadhaar Verified")
                    TrustPill(icon = Icons.Filled.Shield, label = "0% Commission")
                }
            }
        }
    }
}

@Composable
private fun TrustPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x2EFFFFFF)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Universal Brand Top Bar for secondary screens (Worker List, Profile, Requests):
 * Integrates Logo, Name, Tagline, Location, and Topbar accessories.
 */
@Composable
fun HelpSetuTopBar(
    subtitle: String = "Find Trusted Local Help Instantly",
    onBack: (() -> Unit)? = null,
    backButtonTag: String = "topbar_back_button",
    currentLocation: String? = null,
    unreadCount: Int = 0,
    unreadNotificationsCount: Int = unreadCount,
    selectedLanguage: String = "HI",
    onLocationClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = onNotificationClick,
    actions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val effectiveUnread = if (unreadNotificationsCount != 0) unreadNotificationsCount else unreadCount
    val effectiveNotificationClick = if (onNotificationsClick != onNotificationClick) onNotificationsClick else onNotificationClick

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Side: Back button (if any) + Brand Logo + Name + Tagline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag(backButtonTag)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Slate800
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // Logo Icon in circular card
                    Surface(
                        shape = CircleShape,
                        color = Emerald50,
                        border = BorderStroke(1.dp, Emerald100),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            HelpSetuLogoIcon(size = 24.dp, isOnDark = false)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        HelpSetuWordmark(fontSize = 18.sp, isOnDark = false)
                        Text(
                            text = subtitle,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right Side: Location, Notifications, or Custom Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (currentLocation != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Emerald50,
                            border = BorderStroke(1.dp, Emerald100),
                            modifier = Modifier
                                .clickable { onLocationClick() }
                                .testTag("topbar_compact_location")
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
                            }
                        }
                    }

                    // Notifications
                    IconButton(
                        onClick = effectiveNotificationClick,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("topbar_compact_notifications")
                    ) {
                        BadgedBox(
                            badge = {
                                if (effectiveUnread > 0) {
                                    Badge(containerColor = HelpSetuOrange) {
                                        Text(text = "$effectiveUnread", fontSize = 9.sp)
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

                    // Additional Screen-specific Actions
                    actions?.invoke(this)
                }
            }
        }
    }
}

/**
 * Interactive Location Selector Dialog:
 * Allows user to pick active local area across India or enter custom locality
 */
@Composable
fun HelpSetuLocationDialog(
    isOpen: Boolean,
    currentLocation: String,
    onDismiss: () -> Unit,
    onSelectLocation: (String) -> Unit
) {
    if (!isOpen) return

    val popularLocations = listOf(
        "Noida, Sector 62 (Delhi NCR)",
        "Connaught Place, New Delhi",
        "Cyber City, Sector 29 (Gurugram)",
        "Indirapuram, Ghaziabad",
        "Koramangala, Bengaluru",
        "Andheri West, Mumbai",
        "Shivaji Nagar, Pune",
        "Aliganj, Lucknow",
        "Boring Road, Patna",
        "Malviya Nagar, Jaipur"
    )

    var searchQuery by remember { mutableStateOf("") }
    val filtered = popularLocations.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = HelpSetuTeal,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Select Service Location",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Show verified workers near you",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search city or area...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(18.dp),
                            tint = Slate400
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HelpSetuTeal,
                        unfocusedBorderColor = Slate200
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filtered) { loc ->
                        val isSelected = loc.equals(currentLocation, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Emerald50 else Slate50,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) HelpSetuTeal else Slate200
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLocation(loc)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.NearMe,
                                        contentDescription = null,
                                        tint = if (isSelected) HelpSetuTeal else Slate400,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = loc,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) HelpSetuTealDark else Slate800
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = HelpSetuTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (searchQuery.isNotBlank() && !filtered.contains(searchQuery)) {
                        item {
                            OutlinedButton(
                                onClick = {
                                    onSelectLocation(searchQuery)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Use \"$searchQuery\"", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = HelpSetuTeal, fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Interactive HelpSetu Notifications Dialog:
 * Shows recent localized alerts, active worker updates, and safety notices
 */
@Composable
fun HelpSetuNotificationsDialog(
    isOpen: Boolean,
    notifications: List<AppNotification>,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit
) {
    if (!isOpen) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = HelpSetuOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Notifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
                TextButton(onClick = onClearAll) {
                    Text("Clear All", fontSize = 11.sp, color = Slate500)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notif ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate50,
                        border = BorderStroke(1.dp, Slate200),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            val iconVector = when (notif.iconType) {
                                "WORKER" -> Icons.Filled.Engineering
                                "VERIFIED" -> Icons.Filled.Verified
                                "INFO" -> Icons.Filled.Info
                                else -> Icons.Filled.Notifications
                            }
                            val iconColor = when (notif.iconType) {
                                "WORKER" -> HelpSetuTeal
                                "VERIFIED" -> Color(0xFF047857)
                                "INFO" -> HelpSetuOrange
                                else -> Slate600
                            }

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(iconColor.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = null,
                                    tint = iconColor,
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = notif.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                    Text(
                                        text = notif.timeAgo,
                                        fontSize = 10.sp,
                                        color = Slate400
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.description,
                                    fontSize = 11.sp,
                                    color = Slate600,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = HelpSetuTeal),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Got It")
            }
        }
    )
}

/**
 * Aadhaar Verified Badge as shown on Screen 2 in Helpsetu.png:
 * Saffron/Orange fingerprint/sun logo + "Aadhaar Verified" label
 */
@Composable
fun AadhaarVerifiedBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF7ED))
                .border(1.dp, Color(0xFFFDBA74), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = "Aadhaar Verified",
                tint = Color(0xFFEA580C),
                modifier = Modifier.size(13.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = "Aadhaar",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEA580C),
                lineHeight = 10.sp
            )
            Text(
                text = "Verified",
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF047857),
                lineHeight = 10.sp
            )
        }
    }
}

/**
 * Custom Illustrated Graphics for Category 2x2 Grid Cards:
 * Accurately represents the illustrations in Screen 1 of Helpsetu.png:
 * - Electrician: Glowing yellow lightbulb with lightning bolt + slate wrench
 * - Plumber: Water tap with dripping water droplet + wrench
 * - Labourer: Construction worker carrying bricks
 * - Carpenter: Woodwork saw + hammer + wood block
 */
@Composable
fun CategoryIllustratedIcon(
    category: ServiceCategory,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        when (category) {
            ServiceCategory.ELECTRICIAN -> {
                // Lightbulb + Bolt + Spanner
                Canvas(modifier = Modifier.size(size)) {
                    val w = this.size.width
                    val h = this.size.height

                    // 1. Spanner/Wrench (Slate Blue) behind/alongside
                    val wrenchX = w * 0.72f
                    drawLine(
                        color = Color(0xFF475569),
                        start = Offset(wrenchX, h * 0.28f),
                        end = Offset(wrenchX, h * 0.85f),
                        strokeWidth = w * 0.10f,
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = Color(0xFF475569),
                        radius = w * 0.12f,
                        center = Offset(wrenchX, h * 0.28f),
                        style = Stroke(width = w * 0.08f)
                    )

                    // 2. Lightbulb (Sunny Yellow/Amber)
                    val bulbX = w * 0.38f
                    val bulbY = h * 0.44f
                    val bulbR = w * 0.26f
                    drawCircle(
                        color = Color(0xFFFBBF24),
                        radius = bulbR,
                        center = Offset(bulbX, bulbY)
                    )
                    // Bulb base
                    drawRect(
                        color = Color(0xFF475569),
                        topLeft = Offset(bulbX - w * 0.12f, bulbY + bulbR * 0.7f),
                        size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.18f)
                    )

                    // 3. Lightning bolt inside bulb (Dark slate / orange)
                    val boltPath = Path().apply {
                        moveTo(bulbX + w * 0.04f, bulbY - bulbR * 0.6f)
                        lineTo(bulbX - w * 0.08f, bulbY + w * 0.02f)
                        lineTo(bulbX + w * 0.02f, bulbY + w * 0.02f)
                        lineTo(bulbX - w * 0.04f, bulbY + bulbR * 0.6f)
                        lineTo(bulbX + w * 0.09f, bulbY - w * 0.02f)
                        lineTo(bulbX - w * 0.01f, bulbY - w * 0.02f)
                        close()
                    }
                    drawPath(boltPath, color = Color(0xFF1E293B))

                    // 4. Spark rays around lightbulb
                    val rayColor = Color(0xFFF59E0B)
                    drawLine(
                        color = rayColor,
                        start = Offset(bulbX - bulbR * 1.3f, bulbY - bulbR * 0.8f),
                        end = Offset(bulbX - bulbR * 1.6f, bulbY - bulbR * 1.0f),
                        strokeWidth = w * 0.05f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = rayColor,
                        start = Offset(bulbX, bulbY - bulbR * 1.2f),
                        end = Offset(bulbX, bulbY - bulbR * 1.5f),
                        strokeWidth = w * 0.05f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = rayColor,
                        start = Offset(bulbX - bulbR * 1.4f, bulbY + bulbR * 0.2f),
                        end = Offset(bulbX - bulbR * 1.7f, bulbY + bulbR * 0.2f),
                        strokeWidth = w * 0.05f,
                        cap = StrokeCap.Round
                    )
                }
            }

            ServiceCategory.PLUMBER -> {
                // Water tap with dripping water droplet + wrench
                Canvas(modifier = Modifier.size(size)) {
                    val w = this.size.width
                    val h = this.size.height

                    // 1. Angled Wrench handle (Orange/Wood handle)
                    drawLine(
                        color = Color(0xFFEA580C),
                        start = Offset(w * 0.20f, h * 0.78f),
                        end = Offset(w * 0.55f, h * 0.45f),
                        strokeWidth = w * 0.11f,
                        cap = StrokeCap.Round
                    )

                    // 2. Faucet / Tap (Cyan/Blue metallic)
                    val tapColor = Color(0xFF0284C7)
                    // Horizontal pipe
                    drawRect(
                        color = tapColor,
                        topLeft = Offset(w * 0.35f, h * 0.32f),
                        size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.12f)
                    )
                    // Tap spout curving down
                    drawRect(
                        color = tapColor,
                        topLeft = Offset(w * 0.63f, h * 0.32f),
                        size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.24f)
                    )
                    // Tap handle (Cross valve on top)
                    drawRect(
                        color = Color(0xFF0369A1),
                        topLeft = Offset(w * 0.44f, h * 0.18f),
                        size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.08f)
                    )
                    drawLine(
                        color = Color(0xFF0369A1),
                        start = Offset(w * 0.55f, h * 0.24f),
                        end = Offset(w * 0.55f, h * 0.32f),
                        strokeWidth = w * 0.06f
                    )

                    // 3. Water droplet falling from spout
                    val dropPath = Path().apply {
                        val dropX = w * 0.70f
                        val dropY = h * 0.70f
                        moveTo(dropX, dropY - h * 0.08f)
                        cubicTo(
                            dropX + w * 0.08f, dropY + h * 0.02f,
                            dropX + w * 0.08f, dropY + h * 0.08f,
                            dropX, dropY + h * 0.08f
                        )
                        cubicTo(
                            dropX - w * 0.08f, dropY + h * 0.08f,
                            dropX - w * 0.08f, dropY + h * 0.02f,
                            dropX, dropY - h * 0.08f
                        )
                        close()
                    }
                    drawPath(dropPath, color = Color(0xFF38BDF8))
                }
            }

            ServiceCategory.LABOURER -> {
                // Construction worker carrying red bricks
                Canvas(modifier = Modifier.size(size)) {
                    val w = this.size.width
                    val h = this.size.height

                    // 1. Worker Head (Yellow safety helmet)
                    val headX = w * 0.40f
                    val headY = h * 0.26f
                    drawCircle(
                        color = Color(0xFFFBBF24), // Helmet
                        radius = w * 0.14f,
                        center = Offset(headX, headY)
                    )
                    // Face
                    drawCircle(
                        color = Color(0xFFFDE68A),
                        radius = w * 0.10f,
                        center = Offset(headX, headY + h * 0.04f)
                    )

                    // 2. Worker Body / Torso (Teal shirt)
                    drawLine(
                        color = Color(0xFF0F766E),
                        start = Offset(headX, headY + h * 0.12f),
                        end = Offset(headX, headY + h * 0.38f),
                        strokeWidth = w * 0.16f,
                        cap = StrokeCap.Round
                    )

                    // 3. Arms carrying bricks
                    drawLine(
                        color = Color(0xFF0F766E),
                        start = Offset(headX, headY + h * 0.20f),
                        end = Offset(w * 0.65f, headY + h * 0.18f),
                        strokeWidth = w * 0.09f,
                        cap = StrokeCap.Round
                    )

                    // 4. Stack of red clay bricks
                    val brickColor = Color(0xFFDC2626)
                    val brickX = w * 0.58f
                    val brickY = headY + h * 0.06f
                    drawRect(
                        color = brickColor,
                        topLeft = Offset(brickX, brickY),
                        size = androidx.compose.ui.geometry.Size(w * 0.32f, h * 0.09f)
                    )
                    drawRect(
                        color = Color(0xFFB91C1C),
                        topLeft = Offset(brickX + w * 0.04f, brickY + h * 0.10f),
                        size = androidx.compose.ui.geometry.Size(w * 0.30f, h * 0.09f)
                    )
                    drawRect(
                        color = brickColor,
                        topLeft = Offset(brickX + w * 0.02f, brickY + h * 0.20f),
                        size = androidx.compose.ui.geometry.Size(w * 0.32f, h * 0.09f)
                    )

                    // 5. Worker legs
                    drawLine(
                        color = Color(0xFF1E293B),
                        start = Offset(headX - w * 0.06f, headY + h * 0.38f),
                        end = Offset(headX - w * 0.10f, h * 0.88f),
                        strokeWidth = w * 0.09f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color(0xFF1E293B),
                        start = Offset(headX + w * 0.06f, headY + h * 0.38f),
                        end = Offset(headX + w * 0.10f, h * 0.88f),
                        strokeWidth = w * 0.09f,
                        cap = StrokeCap.Round
                    )
                }
            }

            ServiceCategory.CARPENTER -> {
                // Woodwork saw + wood block + hammer
                Canvas(modifier = Modifier.size(size)) {
                    val w = this.size.width
                    val h = this.size.height

                    // 1. Wood Plank (Warm amber/brown)
                    drawRect(
                        color = Color(0xFFD97706),
                        topLeft = Offset(w * 0.15f, h * 0.65f),
                        size = androidx.compose.ui.geometry.Size(w * 0.70f, h * 0.16f)
                    )

                    // 2. Handsaw Blade (Metallic steel)
                    val sawPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.65f)
                        lineTo(w * 0.75f, h * 0.22f)
                        lineTo(w * 0.80f, h * 0.28f)
                        lineTo(w * 0.32f, h * 0.70f)
                        close()
                    }
                    drawPath(sawPath, color = Color(0xFF64748B))

                    // 3. Saw Handle (Wood/Orange)
                    drawCircle(
                        color = Color(0xFFEA580C),
                        radius = w * 0.12f,
                        center = Offset(w * 0.24f, h * 0.66f),
                        style = Stroke(width = w * 0.06f)
                    )

                    // 4. Hammer (crossing on right)
                    drawLine(
                        color = Color(0xFF92400E), // Wood handle
                        start = Offset(w * 0.75f, h * 0.85f),
                        end = Offset(w * 0.55f, h * 0.35f),
                        strokeWidth = w * 0.08f,
                        cap = StrokeCap.Round
                    )
                    drawRect(
                        color = Color(0xFF334155), // Steel head
                        topLeft = Offset(w * 0.45f, h * 0.30f),
                        size = androidx.compose.ui.geometry.Size(w * 0.20f, h * 0.10f)
                    )
                }
            }
        }
    }
}
