package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    Column {

        Button(onClick = { navController.navigate("home") }) {
            Text("Go to Home Screen")
        }

    }
}