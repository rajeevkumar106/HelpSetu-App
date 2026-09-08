package com.helpsetu.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.helpsetu.app.presentation.auth.LoginScreen
import com.helpsetu.app.presentation.requests.RequestsScreen
import com.helpsetu.app.presentation.worker.CategorySelectionScreen
import com.helpsetu.app.presentation.worker.EditProfileScreen
import com.helpsetu.app.presentation.worker.WorkerListScreen
import com.helpsetu.app.presentation.worker.WorkerViewModel

@Composable
fun HelpSetuApp(
    navController: NavHostController = rememberNavController(),
    viewModel: WorkerViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val requests by viewModel.serviceRequests.collectAsState()

    val showBottomBar = currentRoute in Screen.bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                HelpSetuBottomBar(
                    currentRoute = currentRoute,
                    requestsCount = requests.size,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Screen 1: Category Selection (Home - 2x2 Grid View)
            composable(Screen.Home.route) {
                CategorySelectionScreen(
                    viewModel = viewModel,
                    onCategorySelected = { categoryId ->
                        navController.navigate(Screen.Workers.route)
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Profile.route)
                    },
                    onNavigateToRequests = {
                        navController.navigate(Screen.Requests.route)
                    }
                )
            }

            // Screen 2: Results & Connection (Workers List)
            composable(Screen.Workers.route) {
                WorkerListScreen(
                    viewModel = viewModel,
                    onBackToCategories = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 3: My Requests (Recent Contacts / Customer Logs)
            composable(Screen.Requests.route) {
                RequestsScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route)
                    }
                )
            }

            // Screen 4: Edit / Register Worker Profile
            composable(Screen.Profile.route) {
                EditProfileScreen(
                    viewModel = viewModel,
                    onNavigateToWorkers = {
                        navController.navigate(Screen.Workers.route)
                    }
                )
            }

            // Screen 5: Login / Role Selection
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = viewModel,
                    onContinue = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
