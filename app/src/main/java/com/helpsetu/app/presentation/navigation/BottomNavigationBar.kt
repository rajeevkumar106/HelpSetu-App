package com.helpsetu.app.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.HelpSetuAmber
import com.example.ui.theme.HelpSetuGreen
import com.example.ui.theme.HelpSetuSurface
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900

@Composable
fun HelpSetuBottomBar(
    currentRoute: String?,
    requestsCount: Int = 0,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = HelpSetuSurface,
        shadowElevation = 0.dp
    ) {
        androidx.compose.foundation.layout.Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = Slate200
            )

            NavigationBar(
                modifier = Modifier.testTag("bottom_navigation_bar"),
                windowInsets = WindowInsets.navigationBars,
                containerColor = HelpSetuSurface,
                tonalElevation = 0.dp
            ) {
                Screen.bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route

                    NavigationBarItem(
                        modifier = Modifier.testTag("nav_item_${screen.route}"),
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                onNavigateToRoute(screen.route)
                            }
                        },
                        icon = {
                            if (screen == Screen.Requests && requestsCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = HelpSetuAmber,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = requestsCount.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = HelpSetuGreen,
                            selectedTextColor = Slate900,
                            indicatorColor = Emerald100,
                            unselectedIconColor = Slate500.copy(alpha = 0.6f),
                            unselectedTextColor = Slate500
                        )
                    )
                }
            }
        }
    }
}
