package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RecordScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Select Recording Method",
            )
            Button(
                onClick = { navigate(navController, "manualrecord") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ){
                Text("Manual Entry")
            }
            Button(
                onClick = { navigate(navController, "autorecord") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ){
                Text("Automatic Entry")
            }
        }


//        Button(
//            onClick = { navigate(navController, "manualrecord") }
//        ){
//            Text("Manual Entry")
//        }
//
//        Button(onClick = { navigate(navController, "autorecord")}) {
//            Text("Automatic Entry")
//        }
    }

}