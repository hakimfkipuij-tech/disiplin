package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.ApprovalPortalScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ViolationInputScreen
import com.example.ui.theme.AuthorityAmber
import com.example.ui.theme.PrincipalNavy
import com.example.ui.theme.PrincipalNavyContainer
import com.example.ui.viewmodel.SchoolDisciplineViewModel

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Approval : Screen("approval", "Persetujuan")
    object Input : Screen("input", "Input Kasus")
}

@Composable
fun AppNavigation(
    viewModel: SchoolDisciplineViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route
    val pendingApprovals by viewModel.pendingApprovals.collectAsStateWithLifecycle()
    val pendingCount = pendingApprovals.size

    val navItems = listOf(
        Triple(
            Screen.Dashboard,
            Icons.Filled.Dashboard,
            Icons.Outlined.Dashboard
        ),
        Triple(
            Screen.Approval,
            Icons.Filled.Gavel,
            Icons.Outlined.Gavel
        ),
        Triple(
            Screen.Input,
            Icons.Filled.AddCircle,
            Icons.Outlined.AddCircleOutline
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                navItems.forEach { (screen, filledIcon, outlinedIcon) ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            if (screen == Screen.Approval && pendingCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = AuthorityAmber,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = "$pendingCount",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (selected) filledIcon else outlinedIcon,
                                        contentDescription = screen.title
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (selected) filledIcon else outlinedIcon,
                                    contentDescription = screen.title
                                )
                            }
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrincipalNavy,
                            selectedTextColor = PrincipalNavy,
                            indicatorColor = PrincipalNavyContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Screen 1: Dashboard Utama Kepala Sekolah (Initial Screen)
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToApproval = {
                        navController.navigate(Screen.Approval.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToInput = {
                        navController.navigate(Screen.Input.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Screen 2: Portal Persetujuan (Approval) Kepala Sekolah
            composable(Screen.Approval.route) {
                ApprovalPortalScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 3: Form Input Pelanggaran
            composable(Screen.Input.route) {
                ViolationInputScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    },
                    onNavigateToApproval = {
                        navController.navigate(Screen.Approval.route) {
                            popUpTo(Screen.Dashboard.route)
                        }
                    },
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
