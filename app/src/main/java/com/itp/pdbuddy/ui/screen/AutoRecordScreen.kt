package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import com.itp.pdbuddy.utils.navigate

@Composable
fun AutoRecordScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    var loading by remember { mutableStateOf(false) }
    var infotext by remember {
        mutableStateOf("Connecting to machine")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = infotext,
            Modifier.padding(20.dp)
        )
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

    }
}