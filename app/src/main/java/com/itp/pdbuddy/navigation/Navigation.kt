package com.itp.pdbuddy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itp.pdbuddy.ui.screen.HomeScreen
import com.itp.pdbuddy.ui.screen.LoginScreen
import com.itp.pdbuddy.ui.screen.ProfileScreen
import com.itp.pdbuddy.ui.screen.SplashScreen


data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val screen: @Composable (NavHostController) -> Unit
)

object NavigationConfig {
    val navItems = listOf(
        NavItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home
        ) { navController -> HomeScreen(navController) },
        NavItem(
            route = "profile",
            title = "Profile",
            icon = Icons.Default.Person
        ) { navController -> ProfileScreen(navController)},
        NavItem(
            route = "splash",
            title = "Splash",
            icon = Icons.Default.SmartScreen
        ) { navController -> SplashScreen(navController) },
        NavItem(
            route = "login",
            title = "Login",
            icon = Icons.AutoMirrored.Filled.Login
        ) { navController -> LoginScreen(navController) },
    )
}


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {

        NavigationConfig.navItems.forEach { navItem ->
            composable(navItem.route) { navItem.screen(navController) }
        }

    }
}