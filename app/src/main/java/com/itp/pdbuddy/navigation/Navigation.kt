package com.itp.pdbuddy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartScreen
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.itp.pdbuddy.ui.screen.CartSuppliesScreen
import com.itp.pdbuddy.ui.screen.CurrentSuppliesScreen
import com.itp.pdbuddy.ui.screen.HomeScreen
import com.itp.pdbuddy.ui.screen.PastSuppliesScreen
import com.itp.pdbuddy.ui.screen.PaymentScreen
import com.itp.pdbuddy.ui.screen.ProfileScreen
import com.itp.pdbuddy.ui.screen.SplashScreen
import com.itp.pdbuddy.ui.screen.SuppliesScreen
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel


data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val screen: @Composable (NavHostController) -> Unit,
    val children: List<NavItem> = emptyList()
)


object NavigationConfig {
    val navItems = listOf(
        NavItem(
            route = "splash",
            title = "Splash",
            icon = Icons.Default.SmartScreen,
            screen = { navController -> SplashScreen(navController = navController) },
        ),
        NavItem(
            route = "login",
            title = "Splash",
            icon = Icons.Default.SmartScreen,
            screen = { navController -> SplashScreen(navController = navController) },
        ),
        NavItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home,
            screen = { navController -> HomeScreen(navController = navController) }
        ),
        NavItem(
            route = "profile",
            title = "Profile",
            icon = Icons.Default.Person,
            screen = { navController -> ProfileScreen(navController = navController) }
        ),
        NavItem(
            route = "resources",
            title = "Resources",
            icon = Icons.Default.History,
            screen = { navController -> HomeScreen(navController = navController) },
            children = listOf(
                NavItem(
                    route = "resourcesDiet",
                    title = "Dietary",
                    icon = Icons.Default.LocalDining,
                    screen = { navController -> HomeScreen(navController = navController) },
                    children = listOf(
                        NavItem(
                            route = "resourceDietCalculator",
                            title = "Dietary Calculator",
                            icon = Icons.Default.Calculate,
                            screen = { navController -> HomeScreen(navController = navController) }
                        )
                    )
                ),
                NavItem(
                    route = "resourcesTraining",
                    title = "Training",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    screen = { navController -> HomeScreen(navController = navController) }
                ),
                NavItem(
                    route = "resourcesTraining",
                    title = "Training",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    screen = { navController -> HomeScreen(navController = navController) }

                )
            )
        ),
        NavItem(
            route = "supplies",
            title = "Supplies",
            icon = Icons.TwoTone.Inventory2,
            screen = { navController -> SuppliesScreen(navController = navController) }

        ),
        NavItem(
            route = "currentSupplies",
            title = "Current Supplies",
            screen = { navController -> CurrentSuppliesScreen(navController = navController) }

        ),
        NavItem(
            route = "pastSupplies",
            title = "Past Supplies",
            screen = { navController -> PastSuppliesScreen(navController = navController) }

        ),
        NavItem(
            route = "cartSupplies",
            title = "Cart Supplies",
            screen = { navController -> CartSuppliesScreen(navController = navController) }

        ),
        NavItem(
            route = "payment",
            title = "Payment",
            screen = { navController -> PaymentScreen(navController = navController) }

        )


    )
}


fun NavGraphBuilder.addNavItems(navController: NavHostController, navItems: List<NavItem>) {
    navItems.forEach { navItem ->
        if (navItem.children.isEmpty()) {
            composable(navItem.route) { navItem.screen(navController) }
        } else {
            navigation(startDestination = navItem.children.first().route, route = navItem.route) {
                addNavItems(navController, navItem.children)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
        addNavItems(navController, NavigationConfig.navItems)
    }
}
