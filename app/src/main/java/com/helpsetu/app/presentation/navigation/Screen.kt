package com.helpsetu.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val titleHindi: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "HOME",
        titleHindi = "होम",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Workers : Screen(
        route = "workers",
        title = "FIND HELP",
        titleHindi = "कामगार",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )

    object Requests : Screen(
        route = "requests",
        title = "MY REQUESTS",
        titleHindi = "मेरे संपर्क",
        selectedIcon = Icons.Filled.Assignment,
        unselectedIcon = Icons.Outlined.Assignment
    )

    object Profile : Screen(
        route = "profile",
        title = "PROFILE",
        titleHindi = "प्रोफाइल",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    object Login : Screen(
        route = "login",
        title = "ACCOUNT",
        titleHindi = "लॉगिन",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    companion object {
        // Exactly matches the 3-tab Bottom Bar from Helpsetu.png
        val bottomNavItems = listOf(Home, Requests, Profile)
    }
}
