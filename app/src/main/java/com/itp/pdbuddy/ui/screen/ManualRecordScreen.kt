package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import com.itp.pdbuddy.utils.navigate

@Composable
fun ManualRecordScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    Column{
        Text(text = "Manual Recording")
    }
}