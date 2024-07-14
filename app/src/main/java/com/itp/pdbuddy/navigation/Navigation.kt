package com.itp.pdbuddy.navigation

import PastSuppliesScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartScreen
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.itp.pdbuddy.data.repository.Resource
import com.itp.pdbuddy.ui.screen.AdditionalMaterialScreen
import com.itp.pdbuddy.ui.screen.CartSuppliesScreen
import com.itp.pdbuddy.ui.screen.CurrentSuppliesScreen
import com.itp.pdbuddy.ui.screen.HomeScreen
import com.itp.pdbuddy.ui.screen.OrderDetailsScreen
import com.itp.pdbuddy.ui.screen.PaymentScreen
import com.itp.pdbuddy.ui.screen.ProfileScreen
import com.itp.pdbuddy.ui.screen.SplashScreen
import com.itp.pdbuddy.ui.screen.RecordScreen
import com.itp.pdbuddy.ui.screen.ManualRecordScreen
import com.itp.pdbuddy.ui.screen.AutoRecordScreen
import com.itp.pdbuddy.ui.screen.DietScreen
import com.itp.pdbuddy.ui.screen.RecordSuccessScreen
import com.itp.pdbuddy.ui.screen.ResourcesScreen
import com.itp.pdbuddy.ui.screen.SuppliesScreen
import com.itp.pdbuddy.ui.screen.TrainingScreen
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel
import com.itp.pdbuddy.ui.screen.NewNotificationScreen
import com.itp.pdbuddy.ui.screen.RecordSuccessScreen
import com.itp.pdbuddy.ui.screen.NotificationScreen
import com.itp.pdbuddy.ui.screen.PrescriptionHistoryScreen
import com.itp.pdbuddy.ui.screen.PrescriptionManagementScreen
import com.itp.pdbuddy.ui.screen.PrescriptionManualScreen
import com.itp.pdbuddy.ui.screen.SuppliesScreen
import com.itp.pdbuddy.ui.screen.TravelDetailsScreen
import com.itp.pdbuddy.ui.screen.TravelRequestScreen
import com.itp.pdbuddy.ui.screen.TravelScreen


data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val screen: (@Composable (NavHostController) -> Unit)? = null,
    val screenWithParam: (@Composable (NavHostController, NavBackStackEntry) -> Unit)? = null,
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
            route = "record",
            title = "record",
            icon = Icons.Default.Person,
            screen = { navController -> RecordScreen(navController = navController) }
        ),
        NavItem(
            route = "manualrecord",
            title = "manualrecord",
            icon = Icons.Default.Person,
            screen = { navController -> ManualRecordScreen(navController = navController) }
        ),
        NavItem(
            route = "autorecord",
            title = "autorecord",
            icon = Icons.Default.Person,
            screen = { navController -> AutoRecordScreen(navController = navController) }
        ),
        NavItem(
            route = "recordsuccess",
            title = "recordsuccess",
            icon = Icons.Default.Person,
            screen = { navController -> RecordSuccessScreen(navController = navController) }
        ),
        NavItem(
            route = "resources",
            title = "Resources",
            icon = Icons.Default.History,
            screen = { navController -> ResourcesScreen(navController = navController) }
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

        ),
        NavItem(
            route = "orderDetails/{orderId}",
            title = "Order Details",
            screenWithParam = { navController, backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                OrderDetailsScreen(orderId = orderId)
            }
        ),
        NavItem(
            route = "diet",
            title = "diet",
            screen = { navController -> DietScreen(navController = navController)}
        ),
        NavItem(
            route = "training",
            title = "training",
            screen = { navController -> TrainingScreen(navController = navController) }
        ),
        NavItem(
            route = "additionalmaterial",
            title = "additionalmaterial",
            screen = { navController -> AdditionalMaterialScreen(navController = navController) }
        ),
        NavItem(
            route = "travel",
            title = "Travel",
            screen = { navController -> TravelScreen(navController = navController) }
        ),
        NavItem(
            route = "travelDetails/{country}",
            title = "Travel Details",
            screenWithParam = { navController, backStackEntry ->
                val country = backStackEntry.arguments?.getString("country") ?: ""
                TravelDetailsScreen(navController = navController, country = country)
            }
        ),
        NavItem(
            route = "travelRequest",
            title = "Travel Request",
            screen = { navController -> TravelRequestScreen(navController = navController) }

        ),
        NavItem(
            route = "setNotification",
            title = "setNotification",
            screen = { navController -> NotificationScreen(navController = navController) }
        ),
        NavItem(
            route = "newNotification",
            title = "newNotification",
            screen = { navController -> NewNotificationScreen(navController = navController) }
        ),
        NavItem(
            route = "prescription",
            title = "Prescription",
            screen = { navController -> PrescriptionManagementScreen(navController = navController) }
        ),
        NavItem(
            route = "prescriptionManual",
            title = "Manual Update",
            screen = { navController -> PrescriptionManualScreen(navController = navController) }
        ),
        NavItem(
            route = "prescriptionHistory",
            title = "History",
            screen = { navController -> PrescriptionHistoryScreen(navController = navController) }
        )
    )
}


fun NavGraphBuilder.addNavItems(navController: NavHostController, navItems: List<NavItem>) {
    navItems.forEach { navItem ->
        if (navItem.children.isEmpty()) {
            navItem.screen?.let { screen ->
                composable(navItem.route) {
                    screen(navController)
                }
            }
            navItem.screenWithParam?.let { screenWithParam ->
                composable(navItem.route) { backStackEntry ->
                    screenWithParam(navController, backStackEntry)
                }
            }
        } else {
            navigation(startDestination = navItem.children.first().route, route = navItem.route) {
                addNavItems(navController, navItem.children)
            }
        }
    }
}


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        addNavItems(navController, NavigationConfig.navItems)
    }
//    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
//        addNavItems(navController, NavigationConfig.navItems)
//    }
}
