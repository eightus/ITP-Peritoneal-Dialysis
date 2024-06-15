package com.itp.pdbuddy.utils

import androidx.navigation.NavController

fun navigate(navController: NavController, route: String, requireSaveState: Boolean = true) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = requireSaveState
        }
        launchSingleTop = true
        restoreState = requireSaveState
    }
}
